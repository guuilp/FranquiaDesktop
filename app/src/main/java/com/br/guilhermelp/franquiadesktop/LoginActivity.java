package com.br.guilhermelp.franquiadesktop;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;

import com.br.guilhermelp.franquiadesktop.helper.SessionManager;
import com.br.guilhermelp.franquiadesktop.model.Cliente;
import com.br.guilhermelp.franquiadesktop.model.Login;
import com.orm.SugarContext;
import com.orm.SugarRecord;

import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Guilherme on 21/11/2016.
 */

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    private SessionManager session;

    @BindView(R.id.input_email) EditText _emailText;
    @BindView(R.id.loginActivity) ScrollView _scrollView;
    @BindView(R.id.input_password) EditText _passwordText;
    @BindView(R.id.btn_login) Button _loginButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        SugarContext.init(this);

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(_loginButton.getWindowToken(), 0);
            }
        });

        session = new SessionManager(getApplicationContext());

        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void login() {
        Log.d(TAG, "Login");

        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);

        final String usuario = _emailText.getText().toString();
        final String senha = _passwordText.getText().toString();

        progressDialog.setIndeterminate(true);

        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        if(activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
            progressDialog.setMessage("Autenticando...");
            progressDialog.show();
            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            Franquia franquia = null;
                            try {
                                franquia = new ExtratorFranquiaService().execute(usuario, senha).get();
                                if(franquia.getFranquia() != null){
                                    onLoginSuccess(usuario, senha);
                                } else {
                                    onLoginFailed();
                                }

                            }  catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }

                            progressDialog.dismiss();
                        }
                    }, 2000);
        } else {
            Snackbar.make(_scrollView, "Você não está conectado à internet", Snackbar.LENGTH_LONG).show();
            _loginButton.setEnabled(true);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess(String usuario, String senha) throws ExecutionException, InterruptedException {
        Login login = SugarRecord.findById(Login.class, 1);

        if(login == null){
            login = new Login();
            login.setId(1L);
        }

        login.setUsuario(usuario);
        login.setSenha(senha);
        login.save();

        Cliente cliente = SugarRecord.findById(Cliente.class, 1);

        if(cliente == null){
            cliente = new Cliente();
            cliente.setId(1L);
        }

        cliente.setNome(new ExtratorClienteService().execute(usuario, senha).get());
        cliente.save();

        _loginButton.setEnabled(true);
        session.setLogin(true);
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void onLoginFailed() {
        Snackbar.make(_scrollView, "Senha incorreta", Snackbar.LENGTH_LONG).show();
        _loginButton.setEnabled(true);
    }
}
