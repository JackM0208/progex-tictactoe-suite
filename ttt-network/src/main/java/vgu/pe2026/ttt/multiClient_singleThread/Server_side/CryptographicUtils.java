package vgu.pe2026.ttt.multiClient_singleThread.Server_side;

import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import javax.crypto.Mac;

public class CryptographicUtils{
    private static final String SECRET_KEY = "10423191@student.vgu.edu.vn";

    public static String generateSignature(String boardState){
        try{
            SecretKeySpec keySpec = new SecretKeySpec(SECRET_KEY.getBytes(), "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");

            mac.init(keySpec);
            byte[] signatureBytes = mac.doFinal(boardState.getBytes("UTF-8"));

            return Base64.getEncoder().encodeToString(signatureBytes);
        } catch (Exception e){
            throw new RuntimeException("Error in generating signature", e);
        }   
    }

    public static String generateToken(String boardState, long time){
        try{
            SecretKeySpec keySpec = new SecretKeySpec(SECRET_KEY.getBytes(), "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");

            mac.init(keySpec);
            mac.update(boardState.getBytes());
            mac.update(String.valueOf(time).getBytes());

            byte[] token = mac.doFinal();
            return Base64.getEncoder().encodeToString(token);

        } catch (Exception e){
            throw new RuntimeException("Error in generating signature", e);
        }
    }

    public static boolean verifySignature(String boardState, String receivedSignature){

        if(receivedSignature == null){
            System.out.println("Error: Client did not send a signature.");
            return false;
        }

        String realSignature = generateSignature(boardState);
        if(realSignature.equals(receivedSignature)){
            return true;
        }

        return false;
    }

    public static boolean verifyToken(String boardState, long time, String receivedToken){

        if(receivedToken == null){
            System.out.println("Error: Client did not send a token.");
            return false;
        }

        String realToken = generateToken(boardState, time);
        if(realToken.equals(receivedToken)){
            return true;
        }

        return false;
    }

}