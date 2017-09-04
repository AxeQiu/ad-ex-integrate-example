package me.qiuyue.tech;




import java.io.IOException;

interface PowershellProcessProvider {

    /**
     * Get the regular powershell process
     */
    public Process getRegularShell() throws IOException;

    /**
     * Get the Exchange Management Shell process
     */
    public Process getExchangeMgtShell() throws IOException;

}
