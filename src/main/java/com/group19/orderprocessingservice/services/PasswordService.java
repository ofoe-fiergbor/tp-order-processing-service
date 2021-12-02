package com.group19.orderprocessingservice.services;


import com.group19.orderprocessingservice.domain.model.ConfirmationToken;
import com.group19.orderprocessingservice.domain.model.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

@AllArgsConstructor
@Service
public class PasswordService {
    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailService emailService;
    private final EmailValidator emailValidator;


    //login verification
    public String login(UserEntity user){
        UserEntity existingUser = userService.findUserByEmail(user.getEmail());
        if (existingUser!=null){
            if (bCryptPasswordEncoder.matches(user.getPassword(), existingUser.getPassword())){

                return "Login success";
            } else {

                return "Try again";
            }
        } else {

            return "Email does not exist";
        }
    }




    public String forgotUserPassword(UserEntity user) {
        UserEntity existingUser = userService.findUserByEmail(user.getEmail());
        if (existingUser != null) {
            // Create token
            // Save it
            String token= userService.GenerateAndSaveToken(existingUser);
            // Create the email
            emailGeneratorWithToken(existingUser, token);

            return "message - Request to reset password received. Check your inbox for the reset link." + "successForgotPassword";


        } else {
            return  "message - This email address does not exist! -error";
        }

    }

    private void emailGeneratorWithToken(UserEntity existingUser, String token) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(existingUser.getEmail());
        mailMessage.setSubject("Complete Password Reset!");
        mailMessage.setFrom("test-email@gmail.com");
        mailMessage.setText("To complete the password reset process, please click here: "
                + "http://localhost:8080/confirm-reset?token="+confirmationTokenService.getToken(token));
        // Send the email
        emailService.sendEmail(mailMessage);
    }

    public String validateResetToken(@RequestParam("token")String confirmationToken) {
        ConfirmationToken token = confirmationTokenService.getToken(confirmationToken).orElseThrow(()->new UsernameNotFoundException("User not found"));

        if (token != null) {
            UserEntity user = userService.findUserByEmail(token.getUser().getEmail());
            user.setEnabled(true);
            userService.saveUser(user);
            return "user "+ user+" emailId "+user.getEmail()+" resetPassword ";
        } else {
            return "message "+ " The link is invalid or broken! "+ "error";
        }
    }



    public String resetUserPassword( UserEntity user) {
        if (user.getEmail() != null) {

            var exist= emailValidator.test(user.getEmail());
            if (exist){
                // Use email to find user
                UserEntity tokenUser = userService.findUserByEmail(user.getEmail());
                tokenUser.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
                userService.saveUser(tokenUser);
                return "message-Password successfully reset. You can now log in with the new credentials.-successResetPassword";
            }else {
                throw new UsernameNotFoundException("Email invalid");
            }
        } else {
            return "message- he link is invalid or broken!- error";
        }
    }

}