package discord;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;

/**
 * A class to automatically download, extract and load Discord's native library.
 * @author JnCrMx, matti
 */
public class LoadNativeLibrary
{
        /**
         * Cached library file, it is not redownloaded if it is present
         */
        static File library;
    
        /**
         * Loads Discord library from file, and copies it to temp system folder, if the file loading fails, the library is downloaded with @JnCrMx's code
         * 
         * @author matti
         * @return
         * @throws IOException 
         */
        public static File getDiscordLibrary() throws IOException{
            if(library!=null)return library;
            try {
                
            String osName = System.getProperty("os.name").toLowerCase(Locale.ROOT);
                if(!osName.contains("windows"))
                {
                    throw new UnsupportedOperationException();
                } 
                String name = "discord_game_sdk";
                String suffix = ".dll";
                /*
                Some systems report "amd64" (e.g. Windows and Linux), some "x86_64" (e.g. Mac OS).
                 */
                String arch = System.getProperty("os.arch").toLowerCase(Locale.ROOT);
                if(arch.equals("amd64"))
                        arch = "x86_64";

                File tempDir = new File(System.getProperty("java.io.tmpdir"), "java-"+name+System.nanoTime());
                if(!tempDir.mkdir())
                        throw new IOException("Cannot create temporary directory");
                
                tempDir.deleteOnExit();
                
                File temp = new File(tempDir, name+suffix);
                temp.deleteOnExit();
                
                // Copy the file into our temporary file
                InputStream is = LoadNativeLibrary.class.getResourceAsStream("/dslibs/discord_game_sdk_"+arch+suffix);
                if(is==null)
                    throw new URISyntaxException("", "is null");
                
                Files.copy(is, temp.toPath());
                library = temp;
                return temp;
            } catch (URISyntaxException | UnsupportedOperationException ex) {
                return downloadDiscordLibrary();
            }
        }
        
        /**
         * @JnCrMx's code, downloads the discord JDK and extract the right version for the host OS
         * 
         * @author JnCrMx
         * @return temporary file reference
         * @throws IOException on temp folder write error
         */
	private static File downloadDiscordLibrary() throws IOException
	{
            System.out.println("Discord Game SDK lib not found, downloading it...");
            // Find out which name Discord's library has (.dll for Windows, .so for Linux)
            String name = "discord_game_sdk";
            String suffix;

            String osName = System.getProperty("os.name").toLowerCase(Locale.ROOT);
            String arch = System.getProperty("os.arch").toLowerCase(Locale.ROOT);

            if(osName.contains("windows"))
            {
                    suffix = ".dll";
            }
            else if(osName.contains("linux"))
            {
                    suffix = ".so";
            }
            else if(osName.contains("mac os"))
            {
                    suffix = ".dylib";
            }
            else
            {
                    throw new RuntimeException("cannot determine OS type: "+osName);
            }

            /*
            Some systems report "amd64" (e.g. Windows and Linux), some "x86_64" (e.g. Mac OS).
            At this point we need the "x86_64" version, as this one is used in the ZIP.
             */
            if(arch.equals("amd64"))
                    arch = "x86_64";

            // Path of Discord's library inside the ZIP
            String zipPath = "lib/"+arch+"/"+name+suffix;

            // Open the URL as a ZipInputStream
            URL downloadUrl = new URL("https://dl-game-sdk.discordapp.net/2.5.6/discord_game_sdk.zip");
            HttpURLConnection connection = (HttpURLConnection) downloadUrl.openConnection();
            connection.setRequestProperty("User-Agent", "discord-game-sdk4j (https://github.com/JnCrMx/discord-game-sdk4j)");
            ZipInputStream zin = new ZipInputStream(connection.getInputStream());

            // Search for the right file inside the ZIP
            ZipEntry entry;
            while((entry = zin.getNextEntry())!=null)
            {
                    if(entry.getName().equals(zipPath))
                    {
                            // Create a new temporary directory
                            // We need to do this, because we may not change the filename on Windows
                            File tempDir = new File(System.getProperty("java.io.tmpdir"), "java-"+name+System.nanoTime());
                            if(!tempDir.mkdir())
                                    throw new IOException("Cannot create temporary directory");
                            tempDir.deleteOnExit();

                            // Create a temporary file inside our directory (with a "normal" name)
                            File temp = new File(tempDir, name+suffix);
                            temp.deleteOnExit();

                            // Copy the file in the ZIP to our temporary file
                            Files.copy(zin, temp.toPath());

                            // We are done, so close the input stream
                            zin.close();
                            
                            //Save the file per future uses @Eyrtxnuo
                            library = temp;
                            
                             System.out.println("Discord Game SDK lib downloaded!");
                             
                            // Return our temporary file
                            return temp;
                    }
                    // next entry
                    zin.closeEntry();
            }
            zin.close();
            // We couldn't find the library inside the ZIP
            return null;
	}
}