/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import Server.MyFile;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

public class Client {
    public static AudioFormat format = new AudioFormat(16000, 8, 2, true, true);
    public static DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
    public static TargetDataLine targetDataLine;
    public static File audioFile = new File("E://fcih//Network2//audio.wav");
    static JFrame jFrame ;
    static JPanel jmessagePanel;
    // Used to track the file (jpanel that has the file name in it on a label).
    public static int fileId = 0;
    // Array list to hold information about the files received.
    static ArrayList<MyFile> myFiles = new ArrayList<>();
    
    public static void main(String[] args) throws IOException {
        // Accessed from within inner class needs to be final or effectively final.
        final File[] fileToSend = new File[1];

        // Set the frame to house everything.
        jFrame = new JFrame("Client");
        // Set the size of the frame.
        jFrame.setSize(450, 450);
        // Make the layout to be box layout that places its children on top of each other.
        jFrame.setLayout(new BoxLayout(jFrame.getContentPane(), BoxLayout.Y_AXIS));
        // Make it so when the frame is closed the program exits successfully.
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Title above panel.
        JLabel jlTitle = new JLabel("File Sender And Voice Message");
        // Change the font family, size, and style.
        jlTitle.setFont(new Font("Arial", Font.BOLD, 20));
        // Add a border around the label for spacing.
        jlTitle.setBorder(new EmptyBorder(20,0,10,0));
        // Make it so the title is centered horizontally.
        jlTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Panel that will hold the title label and the other jpanels.
        jmessagePanel = new JPanel();
        // Make the panel that contains everything to stack its child elements on top of eachother.
        jmessagePanel.setLayout(new BoxLayout(jmessagePanel, BoxLayout.Y_AXIS));

        // Make it scrollable when the data gets in jpanel.
        JScrollPane jScrollPane = new JScrollPane(jmessagePanel);
        // Make it so there is always a vertical scrollbar.
        jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        
        // Panel that contains the buttons.
        JPanel jpFilePanel= new JPanel();
        // Border for panel .
        jpFilePanel.setBorder(new TitledBorder(new LineBorder(Color.black, 5), "File Sender"));
        // Set the size which must be preferred size for within a container.
        jpFilePanel.setMaximumSize(new Dimension(480, 250));

        // Label that has the file name.
        JLabel jlFileName = new JLabel("Choose a file to send.");
        // Change the font.
        jlFileName.setFont(new Font("Arial", Font.BOLD, 16));
        // Make a border for spacing.
        jlFileName.setBorder(new EmptyBorder(0, 50, 10, 50));
        // Center the label on the x axis (horizontally).
        jlFileName.setAlignmentX(Component.CENTER_ALIGNMENT);
        // Create send file button.
        JButton jbSendFile = new JButton("Send File");
        // Set preferred size works for layout containers.
        jbSendFile.setPreferredSize(new Dimension(150, 75));
        // Change the font style, type, and size for the button.
        jbSendFile.setFont(new Font("Arial", Font.BOLD, 16));
        // Make the second button to choose a file.
        JButton jbChooseFile = new JButton("Choose File");
        // Set the size which must be preferred size for within a container.
        jbChooseFile.setPreferredSize(new Dimension(150, 75));
        // Set the font for the button.
        jbChooseFile.setFont(new Font("Arial", Font.BOLD, 16));

        // Add the buttons to the panel.
        jpFilePanel.add(jlFileName);
        jpFilePanel.add(jbSendFile);
        jpFilePanel.add(jbChooseFile);
        
        
        // Panel that contains the buttons.
        JPanel jpVoicePanel= new JPanel();
        // Border for panel .
        jpVoicePanel.setBorder(new TitledBorder(new LineBorder(Color.black, 5), "Voice Message"));
        // Set the size which must be preferred size for within a container.
        jpVoicePanel.setMaximumSize(new Dimension(480, 250));
        
        // Create send file button.
        JButton jbStartRecord = new JButton("Start Record");
        // Set preferred size works for layout containers.
        jbStartRecord.setPreferredSize(new Dimension(150, 75));
        // Change the font style, type, and size for the button.
        jbStartRecord.setFont(new Font("Arial", Font.BOLD, 16));
        // Make the second button to choose a file.
        JButton jbStopRecord = new JButton("Stop Record");
        // Set the size which must be preferred size for within a container.
        jbStopRecord.setPreferredSize(new Dimension(150, 75));
        // Set the font for the button.
        jbStopRecord.setFont(new Font("Arial", Font.BOLD, 16));
        //Add Button To Panel
        jpVoicePanel.add(jbStartRecord);
        jpVoicePanel.add(jbStopRecord);
        


        // Button action for choosing the file.
        // This is an inner class so we need the fileToSend to be final.
        jbChooseFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Create a file chooser to open the dialog to choose a file.
                JFileChooser jFileChooser = new JFileChooser();
                // Set the title of the dialog.
                jFileChooser.setDialogTitle("Choose a file to send.");
                // Show the dialog and if a file is chosen from the file chooser execute the following statements.
                if (jFileChooser.showOpenDialog(null)  == JFileChooser.APPROVE_OPTION) {
                    // Get the selected file.
                    fileToSend[0] = jFileChooser.getSelectedFile();
                    // Change the text of the java swing label to have the file name.
                    jlFileName.setText("The file you want to send is: " + fileToSend[0].getName());
                }
            }
        });


        // Sends the file when the button is clicked.
        jbSendFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // If a file has not yet been selected then display this message.
                if (fileToSend[0] == null) {
                    jlFileName.setText("Please choose a file to send first!");
                    // If a file has been selected then do the following.
                } else {
                    //recieve data from server
                    try {
                        SendFileToServer(fileToSend[0]);
                    } catch (IOException ex) {
                        Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                    }
 
                }
            }
        });
        
        // Start Record when the button is clicked.
        jbStartRecord.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    targetDataLine = (TargetDataLine) AudioSystem.getLine(info);
                    targetDataLine.open();
                    targetDataLine.start();
                    Thread stream = new Thread(new Runnable(){
                        @Override
                        public void run() {
                            AudioInputStream audiostream = new AudioInputStream(targetDataLine);
                            try {
                                AudioSystem.write(audiostream, AudioFileFormat.Type.WAVE, audioFile);
                            } catch (IOException ex) {
                                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        
                    });
                    stream.start();
                } catch (LineUnavailableException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        });
        
         // Stop Record when the button is clicked.
        jbStopRecord.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Thread.sleep(10);
                    targetDataLine.stop();
                    targetDataLine.close();
                    SendFileToServer(audioFile);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                    
            }  

            
        });

        // Add everything to the frame and make it visible.
        jFrame.add(jlTitle);
        jFrame.add(jScrollPane);
        jFrame.add(jpFilePanel);
        jFrame.add(jpVoicePanel);
        jFrame.setVisible(true);
     
        
    }
    
    /**
     * SendFileToServer
     * @param  MyFile
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public static void SendFileToServer(File MyFile) throws FileNotFoundException, IOException {
        // Create an input stream into the file you want to send.
        FileInputStream fileInputStream = new FileInputStream(MyFile.getAbsolutePath());
        //global creation socket
        Socket socket= new Socket("localhost", 8080);
        // Create an output stream to write to write to the server over the socket connection.
        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
        // Get the name of the file you want to send and store it in filename.
        String fileName = MyFile.getName();
        // Convert the name of the file into an array of bytes to be sent to the server.
        byte[] fileNameBytes = fileName.getBytes();
        // Create a byte array the size of the file so don't send too little or too much data to the server.
        byte[] fileBytes = new byte[(int)MyFile.length()];
        // Put the contents of the file into the array of bytes to be sent so these bytes can be sent to the server.
        fileInputStream.read(fileBytes);
        // Send the length of the name of the file so server knows when to stop reading.
        dataOutputStream.writeInt(fileNameBytes.length);
        // Send the file name.
        dataOutputStream.write(fileNameBytes);
        // Send the length of the byte array so the server knows when to stop reading.
        dataOutputStream.writeInt(fileBytes.length);
        // Send the actual file.
        dataOutputStream.write(fileBytes);
        //recieve data from server
        ReceiveDataFromServer(socket);
        
    }
    
    /**
     * ReceiveDataFromServer
     * @param socket
     */
    public static void ReceiveDataFromServer(Socket socket) throws IOException{  
        // Stream to receive data from the client through the socket.
        DataInputStream dataInputStream = new DataInputStream(socket.getInputStream()); 
        Thread recieveThread = new Thread(new Runnable(){
            @Override
            public void run() {
                try{
                    while(true){
                        // Read the size of the file name so know when to stop reading.
                        int fileNameLength = dataInputStream.readInt();
                        // If the file exists
                        if (fileNameLength > 0) {
                            // Byte array to hold name of file.
                            byte[] fileNameBytes = new byte[fileNameLength];
                            // Read from the input stream into the byte array.
                            dataInputStream.readFully(fileNameBytes, 0, fileNameBytes.length);
                            // Create the file name from the byte array.
                            String fileName = new String(fileNameBytes);
                            // Read how much data to expect for the actual content of the file.
                            int fileContentLength = dataInputStream.readInt();
                            // Array to hold the file data.
                            byte[] fileContentBytes = new byte[fileContentLength];
                            // Read from the input stream into the fileContentBytes array.
                            dataInputStream.readFully(fileContentBytes, 0, fileContentBytes.length);
                            // Panel to hold the picture and file name.
                            JPanel jpFileRow = new JPanel();
                            jpFileRow.setLayout(new BoxLayout(jpFileRow, BoxLayout.X_AXIS));
                            // Set the file name.
                            JLabel jlFileName = new JLabel(fileName);
                            jlFileName.setFont(new Font("Arial", Font.BOLD, 20));
                            jlFileName.setBorder(new EmptyBorder(10,0, 10,0));
                            // Set the name to be the fileId so you can get the correct file from the panel.
                            jpFileRow.setName((String.valueOf(fileId)));
                            jpFileRow.addMouseListener(getMyMouseListener());
                            // Add everything.
                            jpFileRow.add(jlFileName);
                            jmessagePanel.add(jpFileRow);
                            jFrame.validate();
                            // Add the new file to the array list which holds all our data.
                            myFiles.add(new MyFile(fileId, fileName, fileContentBytes, getFileExtension(fileName)));
                            // Increment the fileId for the next file to be received.
                            fileId++;

                        }
                    }
                } catch (IOException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    try {
                        dataInputStream.close();
                    } catch (IOException ex) {
                        Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
        }
        });
        recieveThread.start();
    }
    
    /**
     * @param fileName
     * @return The extension type of the file.
     */
    public static String getFileExtension(String fileName) {
        // Get the file type by using the last occurence of . (for example aboutMe.txt returns txt).
        // Will have issues with files like myFile.tar.gz.
        int i = fileName.lastIndexOf('.');
        // If there is an extension.
        if (i > 0) {
            // Set the extension to the extension of the filename.
            return fileName.substring(i + 1);
        } else {
            return "No extension found.";
        }
    }
    
    /**
     * When the jpanel is clicked a popup shows to say whether the user wants to download
     * the selected document.
     *
     * @return A mouselistener that is used by the jpanel.
     */
    public static MouseListener getMyMouseListener() {
        return new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Get the source of the click which is the JPanel.
                JPanel jPanel = (JPanel) e.getSource();
                // Get the ID of the file.
                int fileId = Integer.parseInt(jPanel.getName());
                // Loop through the file storage and see which file is the selected one.
                for (MyFile myFile : myFiles) {
                    if (myFile.getId() == fileId) {
                        Download(myFile.getName(), myFile.getData());
                        if ((myFile.getFileExtension()).equalsIgnoreCase("wav")){
                            playMusic(myFile.getName());
                        }   
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        };
    }
    
    /**
     * Download file
     * 
     * @param String fileName
     * @param byte[] fileData 
     */
    public static void Download(String fileName, byte[] fileData){
        // Create the file with its name.
        File fileToDownload = new File(fileName);
        try {
            // Create a stream to write data to the file.
            FileOutputStream fileOutputStream = new FileOutputStream(fileToDownload);
            // Write the actual file data to the file.
            fileOutputStream.write(fileData);
            // Close the stream.
            fileOutputStream.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * playMusic
     * @param fileName 
     */
    public static void playMusic(String fileName) {
        try {
            File music = new File(fileName);
            AudioInputStream audio = AudioSystem.getAudioInputStream(music);
            Clip clip= AudioSystem.getClip();
            clip.open(audio);
            clip.start();
        } catch (UnsupportedAudioFileException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } catch (LineUnavailableException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } 
            
        
    }
}
    