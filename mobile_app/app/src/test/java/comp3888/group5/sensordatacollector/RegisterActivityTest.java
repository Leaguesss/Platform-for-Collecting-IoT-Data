package comp3888.group5.sensordatacollector;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RegisterActivityTest {

    private RegisterActivity testRister;

    @Before
    public void setUp() throws Exception {
        testRister = new RegisterActivity();
    }

    @Test
    public void testOnCreate() {
        assertNull(testRister.bCancel);
        assertNull(testRister.bRegister);
        assertNull(testRister.bRegister);
        assertNull(testRister.typeEmail);
        assertNull(testRister.typePassword);
        assertNull(testRister.confirmPassword);
        assertNull(testRister.fAuth);

    }

    @Test
    public void isEmailValid1() {
        assertEquals(testRister.isEmailValid("jeffreyzmj@gmail.com"), true);
    }

    @Test
    public void isEmailValid2() {
        assertEquals(testRister.isEmailValid("fdsagag"), false);
    }

    @Test
    public void checkPassLength() {
        assertEquals(testRister.checkPassLength("123456"), false);
    }

    @Test
    public void checkPassLength2() {
        assertEquals(testRister.checkPassLength("12345678"), true);
    }

    @Test
    public void checkPassMatch() {
        assertEquals(testRister.checkPassMatch("12345678", "12345678"), true);
    }

    @Test
    public void checkPassMatch2() {
        assertEquals(testRister.checkPassMatch("12345678", "123678"), false);
    }

    @Test
    public void testOnclick() {

    }


}