package comp3888.group5.sensordatacollector;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;

public class LoginActivityTest {

    private LoginActivity testLogin;

    @Before
    public void setUp() throws Exception {
        testLogin = new LoginActivity();
    }

    @Test
    public void testonCreate() {
        assertNull(testLogin.etUsername);
        assertNull(testLogin.bLogin);
        assertNull(testLogin.bRegister);
        assertNull(testLogin.gSign);
        assertNull(testLogin.fAuth);
        assertNull(testLogin.mGoogleSignInClient);
        assertNull(testLogin.etPassword);
        assertNotNull(testLogin.admin);
        assertNotNull(testLogin.adminpass);
        assertNotNull(testLogin.RC_SIGN_IN);

    }

    @Test
    public void onActivityResult() {
    }

    @Test
    public void onClick() {
    }


}