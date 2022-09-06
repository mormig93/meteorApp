package meteorApp.main;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Image;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import java.awt.Color;
import java.awt.Dialog.ModalExclusionType;
import java.awt.EventQueue;
import java.awt.Toolkit;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.UIManager;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class meteorApp extends JFrame {

	private JPanel contentPane;
	private String ruta = new String();
	public JTextArea textArea = new JTextArea();
	public JFileChooser dir = new JFileChooser();
	public Process proceso;
	
	public String getRuta() {
		return ruta;
	}
	public void setRuta(String ruta) {
		this.ruta = ruta;
	}
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					meteorApp m = new meteorApp();
					m.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
public meteorApp() {
		
		setResizable(false);
		setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Imagenes/meteorjs2.png")));
		setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
		setTitle("MeteorStart");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setToolTipText("");
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panel.setBounds(10, 0, 413, 44);
		contentPane.add(panel);
		
		//Aca se obtiene el directorio del proyecto meteor
		ImageIcon imagen_1 = new ImageIcon(getClass().getResource("/Imagenes/dir.jpg"));
		ImageIcon icono_1 = new ImageIcon(imagen_1.getImage().getScaledInstance(40, 40, Image.SCALE_DEFAULT));
		JButton btnNewButton_1 = new JButton(icono_1);
		btnNewButton_1.setToolTipText("");
		btnNewButton_1.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				dir.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int opcion = dir.showOpenDialog(dir);
				if(opcion == JFileChooser.APPROVE_OPTION) {
					setRuta(dir.getSelectedFile().getPath());
					textArea.setText("Ruta del Proyecto: \n"+getRuta());
				}
			}
		});
		btnNewButton_1.setBackground(UIManager.getColor("Button.disabledShadow"));
		btnNewButton_1.setBounds(0, 2, 120, 40);
		
		//Aca se ejecuta meteor en el directorio especificado
		ImageIcon imagen = new ImageIcon(getClass().getResource("/Imagenes/play.png"));
		ImageIcon icono = new ImageIcon(imagen.getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT));
		JButton btnNewButton = new JButton(icono);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!getRuta().equals("")) {
					ExecutorService executor = Executors.newSingleThreadExecutor();
					executor.submit(() -> {
						try {
							textArea.setText("Iniciando MeteorJS . . .\n");
							proceso = Runtime.getRuntime().exec("cmd /c cd && meteor",null,new File(getRuta()));
							printResults(proceso,textArea);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					});					
				}else {
					textArea.setText("Aun no hay directorio.");
				}
			}
		});
		btnNewButton.setBackground(UIManager.getColor("Button.disabledShadow"));
		btnNewButton.setForeground(Color.BLACK);
		btnNewButton.setBounds(148, 2, 120, 40);
		
		//Aca se detiene la ejecucion del proyecto
		ImageIcon imagen_2 = new ImageIcon(getClass().getResource("/Imagenes/stop.png"));
		ImageIcon icono_2 = new ImageIcon(imagen_2.getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT));
		JButton btnNewButton_2 = new JButton(icono_2);
		btnNewButton_2.addActionListener(new ActionListener() {
			Process cerrar;
			public void actionPerformed(ActionEvent e) {
				if (proceso == null) {
					textArea.setText("No se ha ejecutado ningun proyecto todavia. ");
				}else {
					try {
						cerrar = Runtime.getRuntime().exec("taskkill /f /im node.exe /T");
						cerrar.waitFor();
						if ( cerrar.exitValue() == 0){
							textArea.append("Meteor detenido con exito. \n");
						}else{
							textArea.append("Error. Exit Code: "+cerrar.exitValue()+"\n");
						}
					} catch (IOException e2) {
						e2.printStackTrace();
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		btnNewButton_2.setBackground(UIManager.getColor("Button.disabledShadow"));
		btnNewButton_2.setBounds(293, 2, 120, 40);
		panel.setLayout(null);
		
		panel.add(btnNewButton_1);
		panel.add(btnNewButton);
		panel.add(btnNewButton_2);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(Color.WHITE);
		panel_1.setBounds(10, 55, 413, 205);
		contentPane.add(panel_1);
		panel_1.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 0, 413, 205);
		panel_1.add(scrollPane);
		
		scrollPane.setViewportView(textArea);
	}
	
	//Aca se muestra el contenido emitido por la consola/terminal
	public void printResults(Process process, JTextArea textarea) throws IOException {
	    BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), "UTF-8"));
	    String line = reader.readLine();
	    while (line != null) {
	    	textarea.append(line+"\n");
	    	//System.out.println(line);
	        line = reader.readLine();
	    }
	}	
}
