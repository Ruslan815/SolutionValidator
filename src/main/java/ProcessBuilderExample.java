import java.io.*;

public class ProcessBuilderExample {

    public static void main(String[] args) {



        //new ProcessBuilder(new String[]{"cmd.exe", "/c", "R:\\task1.exe"}).start();
        // System.out.println(System.getProperty("user.dir"));
        //File dir = new File("D:\\bat_scripts");
        //processBuilder.directory(dir);

        String[] inputData = new String[]{"2"};

        ProcessBuilder processBuilder = new ProcessBuilder();
        String executeCommand = "exec\\task1.exe"; // "ping -n 3 yandex.ru"
        processBuilder.command("cmd.exe", "/c", executeCommand);

        try {
            Process process = processBuilder.start();

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
            for (String data : inputData) {
                writer.write(data + "\n");
            }
            writer.flush();
            //writer.close();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            reader.close();

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new ValidatorException("Error while reference file executing.\n" +
                        "Error code:\n" + exitCode);
            }
            if (process.getErrorStream().read() != -1) {
                throw new ValidatorException("Error while reference file executing.\n" +
                        "Error stream:\n" + process.getErrorStream());
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }

}