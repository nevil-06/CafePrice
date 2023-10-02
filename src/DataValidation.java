import java.util.*;

public class DataValidation {

    public static final String delimiter_split_list = "[[ ]*|[,]*|[)]*|[(]*|[\"]*|[;]*|[-]*|[:]*|[']*|[\\.]*|[:]*|[/]*|[!]*|[?]*|[+]*]+";
    public static String[] regexValidate(String text_to_validate){

        String[] text_split = text_to_validate.split(delimiter_split_list);

        return text_split;
    }

    public static void main(String[] args){

        String test = "Arnab()()Prerna;';'HEHE;;";
        String[] res = regexValidate(test);

        for(String s: res){
            System.out.println(s);
        }
    }
}
