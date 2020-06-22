package service;

import domain.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import repository.UserRepository;
import static org.assertj.core.api.Assertions.*;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    public void test_addUser_whenNoUsersArePresent() {

        User user = new User();

        when(userRepository.getNextIdSequence()).thenReturn(1);
        when(userRepository.findByUsername("")).thenReturn(null);

        doThrow(new NullPointerException())
                .doNothing()
                .when(userService).addOrUpdateUser(user);

        verify(userRepository).add(user);
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void test_addUser_whenOneUserIsPresent() {
        User user = new User("kutay", "password", true);

        when(userRepository.getNextIdSequence()).thenReturn(1);
        when(userRepository.findByUsername("")).thenReturn(null);

        userService.addOrUpdateUser(user);

        verify(userRepository).add(user);
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void test_updateUser_whenOneUserIsPresent() {
        User user = new User("kutay", "password");
        User newUser = new User("kutay", "new_password");

        when(userRepository.getNextIdSequence()).thenReturn(2);
        when(userRepository.findByUsername("")).thenReturn(user);

        userService.addOrUpdateUser(newUser);

        verify(userRepository).update(newUser);
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void test_getUserById() {

        User user = new User("kutay", "password");
        int id = 1;

        when(userRepository.findById(id)).thenReturn(user);

        User expectedUser = userService.findUserById(id).get();

        assertThat(expectedUser).isEqualTo(user);
    }

    @Test
    public void test_getUserByUsername() {

        User user = new User("kutay", "password");
        String username = "kutay";

        when(userRepository.findByUsername(username)).thenReturn(user);

        User expectedUser = userService.findUserByUsername(username).get();

        assertThat(expectedUser).isEqualTo(user);
    }
}
