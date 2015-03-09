package com.citrus.prepaid;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.citrus.asynch.ForgotPass;
import com.citrus.asynch.LinkUser;
import com.citrus.asynch.SetPassword;
import com.citrus.asynch.SignIn;
import com.citrus.asynch.WalletStatus;
import com.citrus.card.Card;
import com.citrus.cash.LoadMoney;
import com.citrus.cash.Prepaid;
import com.citrus.cash.PrepaidPg;
import com.citrus.mobile.Callback;
import com.citrus.mobile.Config;
import com.citrus.netbank.Bank;
import com.citrus.payment.Bill;
import com.citrus.payment.PG;
import com.citrus.payment.UserDetails;
import com.citrus.sample.GetBill;
import com.citrus.sample.R;
import com.citrus.sample.WebPage;

public class PrepaidWallet extends Activity {
	
	private static final String bill_url = "http://yourwebsite.com/bill.php";
	
	Button isSignedin, linkuser, setpass, forgot, signin, getbalance
	,card_load, token_load, bank_load, citrus_cashpay, get_prepaidToken;

	Callback callback;
	
	String prepaid_bill;
	
	JSONObject customer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_prepaid);
		
		isSignedin = (Button) this.findViewById(R.id.issignedin);
		
		linkuser = (Button) this.findViewById(R.id.linkuser);
		
		setpass = (Button) this.findViewById(R.id.setpassword);
		
		forgot = (Button) this.findViewById(R.id.forgot);
		
		signin = (Button) this.findViewById(R.id.signin);
		
		getbalance = (Button) this.findViewById(R.id.getbalance);
				
		card_load = (Button) this.findViewById(R.id.cardload);
		
		token_load = (Button) this.findViewById(R.id.tokenload);
		
		bank_load = (Button) this.findViewById(R.id.bankload);
		
		citrus_cashpay = (Button) this.findViewById(R.id.citruscash);
		
		callback = new Callback() {
			
			@Override
			public void onTaskexecuted(String success, String error) {
					showToast(success, error);
			}
		};
		
		init();
		
		initconfig();
		
		initcustdetails();
	}
	
	private void init() {
		
		isSignedin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new WalletStatus(PrepaidWallet.this, callback).execute();
			}
		});
		
		linkuser.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new LinkUser(PrepaidWallet.this, callback)
				.execute(new String[]{"testeremail@mailinator.com", "9769507476"});
			}
		});
		
		setpass.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new SetPassword(PrepaidWallet.this, callback)
				.execute(new String[]{"testeremail@mailinator.com", "9769507476", "tester@123"});
			}
		});
		
		forgot.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new ForgotPass(PrepaidWallet.this, "testeremail@mailinator.com", callback)
				.execute();
			}
		});
		
		signin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new SignIn(PrepaidWallet.this, callback)
				.execute(new String[]{"testeremail@mailinator.com", "tester@123"});
			}
		});
		
		getbalance.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Prepaid user = new Prepaid("testeremail@mailinator.com");
				user.getBalance(PrepaidWallet.this, callback);
			}
		});
		
		card_load.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Card card = new Card("4111111111111111", "04", "21", "778", "Bruce Banner", "debit");
				
				LoadMoney load = new LoadMoney("10", "http://yourwebsite.com/return_url.php");
				
				UserDetails userDetails = new UserDetails(customer);
				
				PG paymentgateway = new PG(card, load, userDetails);
				
				paymentgateway.load(PrepaidWallet.this, new Callback() {
		            @Override
		            public void onTaskexecuted(String success, String error) {
		                processresponse(success, error);
		            }
		        });
						
			}
		});
		
		token_load.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Card card = new Card("f1b2508e360c345285d7917d4f4eb112", "778");
				
				LoadMoney load = new LoadMoney("100", "http://yourwebsite.com/return_url.php");
				
				UserDetails userDetails = new UserDetails(customer);
				
				PG paymentgateway = new PG(card, load, userDetails);
				
				paymentgateway.load(PrepaidWallet.this, new Callback() {
		            @Override
		            public void onTaskexecuted(String success, String error) {
		                processresponse(success, error);
		            }
		        });
				
			}
		});
		
		bank_load.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Bank netbank = new Bank("CID002");
				
				LoadMoney load = new LoadMoney("100", "http://yourwebsite.com/return_url.php");
				
				UserDetails userDetails = new UserDetails(customer);
				
				PG paymentgateway = new PG(netbank, load, userDetails);
				
				paymentgateway.load(PrepaidWallet.this, new Callback() {
		            @Override
		            public void onTaskexecuted(String success, String error) {
		                processresponse(success, error);
		            }
		        });
				
			}
		});
		
		citrus_cashpay.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new GetBill(bill_url, new Callback() {
					
					@Override
					public void onTaskexecuted(String bill, String error) {
						if (!TextUtils.isEmpty(bill))
							walletpay(bill);
						
						showToast(bill, error);
					}
				})
				.execute();				
			}
		});
		
	}
	
	private void processresponse(String response, String error) {

        if (!TextUtils.isEmpty(response)) {
            try {

                JSONObject redirect = new JSONObject(response);
                Intent i = new Intent(PrepaidWallet.this, WebPage.class);

                if (!TextUtils.isEmpty(redirect.getString("redirectUrl"))) {

                    i.putExtra("url", redirect.getString("redirectUrl"));
                    startActivity(i);
                }
                else {
                    Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        else {
            Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
        }

    }
	
	
	private void initconfig() {
		Config.setEnv("sandbox"); //replace it with "production" when you are ready
        
        /*Replace following details with oauth details provided to you*/
        Config.setupSignupId("test-signup");
        Config.setupSignupSecret("c78ec84e389814a05d3ae46546d16d2e");

        Config.setSigninId("gogo-pre-wallet");
        Config.setSigninSecret("e6f1b840c652d2ffc46530faaac8b771");
	}
	
	private void initcustdetails() {
		customer = new JSONObject();
		/*All the below mentioned parameters are mandatory - missing anyone of them may create errors
	     * Do not change the key in the json below - only change the values*/

	        try {
	            customer.put("firstName", "Tester");
	            customer.put("lastName", "Citrus");
	            customer.put("email", "testeremail@mailinator.com");
	            customer.put("mobileNo", "9769507476");
	            customer.put("street1", "streetone");
	            customer.put("street2", "streettwo");
	            customer.put("city", "Mumbai");
	            customer.put("state", "Maharashtra");
	            customer.put("country", "India");
	            customer.put("zip", "400052");
	        } catch (JSONException e) {
	            e.printStackTrace();
	        }

	}
	
	private void walletpay(String bill_string) {
    	Bill bill = new Bill(bill_string);
    	
    	Prepaid prepaid = new Prepaid("testeremail@mailinator.com");
    	
    	UserDetails userDetails = new UserDetails(customer);

        PG paymentgateway = new PG(prepaid, bill, userDetails);

        paymentgateway.charge(new Callback() {
            @Override
            public void onTaskexecuted(String success, String error) {
                prepaidPayment(success, error);
            }
        });
    }


	private void prepaidPayment(String response, String error) {
    	
    	if (TextUtils.isEmpty(response.toString())) {
    		return;
    	}
    	 
    	Callback prepaidCb = new Callback() {
			
			@Override
			public void onTaskexecuted(String success, String error) {
				showToast(success, error);
			}
		};
		
		PrepaidPg paymentPg = new PrepaidPg(PrepaidWallet.this);
		
		paymentPg.pay(prepaidCb, response, error);
    }
	
	private void showToast(String message, String error) {
		if (!TextUtils.isEmpty(message))
	        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

	    if (!TextUtils.isEmpty(error))
	        Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
	}
}
