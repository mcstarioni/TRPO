package lab1;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
class Assignment2
{
    public static class Part1
    {

        static final String regex = "^(\\d{4})-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])" +
                "(T([0-1][0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])" +
                "([+-](0[1-9]|1[0-2]):([0-5][0-9]))?)?$";
        static final String string = "2004-10-31T22:30:59-10:46";

        public static void main(String[] args)
        {
            Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(string);
            if (matcher.find())
            {
                System.out.println("Full match: " + matcher.group(0));
                for (int i = 1; i <= matcher.groupCount(); i++)
                {
                    System.out.println("Group " + i + ": " + matcher.group(i));
                }
            }


        }
    }
    public static class Part2
    {
        static final String regex = "^(\\w([\\w.-]\\w)*?|\\w){1,}@(\\w([\\w.-]\\w)*.|\\w){1,}(\\w([\\w-]\\w)*|\\w)$";
        static final String string = "dfdf.dfvvf@vfv-f.vfvf.vf";

        public static void main(String[] args)
        {
            final Pattern pattern = Pattern.compile(regex);
            final Matcher matcher = pattern.matcher(string);
            if(matcher.find())
            {
                System.out.println("Full match: " + matcher.group(0));
            }
            else
            {
                System.out.println("Match not found");
            }
        }
    }


}