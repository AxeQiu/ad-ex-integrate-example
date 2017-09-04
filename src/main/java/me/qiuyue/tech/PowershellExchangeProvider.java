package me.qiuyue.tech;



//I/O thins
import java.io.PrintWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.IOException;

//charset thins
import java.nio.charset.Charset;


/**
 *
 */
public class PowershellExchangeProvider
    implements PowershellProcessProvider {

    @Override
    public Process getRegularShell()
    throws IOException {

        ProcessBuilder pb =
            new ProcessBuilder(
                                "powershell",
                                "-NoExit",
                                "-Command",
                                "-");
        Process shellProcess =
            pb.start();

        return shellProcess;
    }



    @Override
    public Process getExchangeMgtShell()
    throws IOException {
        
        Process shell = getRegularShell();

        //get PrintWriter from the regular shell
        PrintWriter pw = new PrintWriter(
                new OutputStreamWriter(
                    shell.getOutputStream(), Charset.forName("GBK")));

        //put the command
        pw.println(". remoteexchange.ps1;Connect-ExchangeServer -auto -ClientApplication:managementShell");

        //flush to stdin
        pw.flush();

        //To Do: Dump all message(for example, welcome message) from the stdout (by shell.getInputStream())

        //
        return shell;
    }
}
