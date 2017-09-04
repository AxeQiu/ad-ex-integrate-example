package me.qiuyue.tech;


//utils
import java.util.Date;

//I/O things
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

//charset things
import java.nio.charset.Charset;

//exception
import java.io.IOException;


/**
 *
 */
public class PowershellUtil {

    private final PowershellProcessProvider provider;
    private final Process regularShell;
    private final Process exchangeShell;

    //constructor
    public PowershellUtil(PowershellProcessProvider p)
    throws IOException {
        this.provider = p;
        this.regularShell = p.getRegularShell();
        this.exchangeShell = p.getExchangeMgtShell();
    }


    /**
     * Executes regular powershell command;
     */
    public String executeRegularCommand(String command, String jobLog, String jobItselfLog)
    throws IOException {
        return execute(command, this.regularShell, jobLog, jobItselfLog);
    }

    /**
     * Executes exchange management powershell command;
     */
    public String executeExchangeCommand(String command, String jobLog, String jobItselfLog)
    throws IOException {
        return execute(command, this.exchangeShell, jobLog, jobItselfLog);
    }


    /**
     *
     */
    protected String execute(String command, Process shell, String jobLogPath, String jobItselfLogPath)
        throws IOException
    {
        String id = String.valueOf(new Date().getTime());
        String fullCommand = getFullCommand(
                id,
                command,
                jobLogPath,
                jobItselfLogPath);
        synchronized(shell)
        {
            PrintWriter pw =
                new PrintWriter(
                        new OutputStreamWriter(
                            shell.getOutputStream(), Charset.forName("GBK")));

            pw.println(fullCommand);

            pw.flush();
        }

        return id;
    }


    /**
     *
     */
    protected static String getFullCommand(
            String id, 
            String command, 
            String jobLogPath, 
            String jobItselfLogPath)
    {
        String fullCommand =
            "Start-Job -ScriptBlock { " +
            "mkdir " + jobLogPath + "/" + id + "; " +
            "Try { " +
            "Write-Output 'running' | Out-File " + jobLogPath + "/" + id + "/status.txt; " +
            command + " 1>" + jobLogPath + "/" + id + "/output.txt " +
            "2> " + jobLogPath + "/" + id + "/error.txt " +
            "3> " + jobLogPath + "/" + id + "/warning.txt " +
            "4> " + jobLogPath + "/" + id + "/debug.txt" +
            "; " +
            "Write-Output 'complete' | Out-File " + jobLogPath + "/" + id + "/status.txt; " +
            "} " +
            "Catch { " +
            "Write-Output 'exception' | Out-File " + jobLogPath + "/" + id + "/status.txt; " +
            "$_ | Out-File " + jobLogPath + "/" + id + "/exception.txt; " +
            "} " + 
            "} | Select-Object * 2>&1 3>&1 4>&1 > " + jobItselfLogPath + "/" + id + ".txt "
            ;

        return fullCommand;
    }
}
