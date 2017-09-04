package me.qiuyue.tech;


import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class PowershellUtilTest {

    @Test
    public void testGetFullCommand() {
        String id = "a1b2c3d4";
        String command = "Get-AdGroup it";
        String jobLogPath = "c:/users/mi/job/";
        String jobItselfLogPath = "c:/users/mi/job/";
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

        assertEquals(fullCommand, PowershellUtil.getFullCommand(id, command, jobLogPath, jobItselfLogPath));

    }
}
