package lab3;


import util.ServletLauncher;

import java.lang.management.ManagementFactory;

public class Launcher
    {
        public static void main(String[] args) throws Exception
        {
            ServletLauncher.launch("web1.xml");
        }
    }
