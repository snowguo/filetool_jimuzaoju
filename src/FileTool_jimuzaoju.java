import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;


public class FileTool_jimuzaoju
{
	static JFrame jFrame = new JFrame();
	static private String fromPath = null;
	static private String toPath = null;
	static private String tempFilePath = null;
	static TextArea textArea;
	public static void main(String[] args)
	{
		
		JButton selectFrom = new javax.swing.JButton();		
		selectFrom.setText("选择导入路径");
		selectFrom.setBounds(10, 10, 200, 50);
		selectFrom.addActionListener(new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				JFileChooser fc = new JFileChooser();
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); 
		    	int i = fc.showDialog(jFrame, "选择导入目录");
		    	if(i == JFileChooser.APPROVE_OPTION) {
					fromPath = fc.getSelectedFile().getPath();
					System.out.println(fromPath);
		    	}				
			}
		});
		
		JButton toFileButton = new javax.swing.JButton();		
		toFileButton.setText("选择导出路径");
		toFileButton.setBounds(280, 10, 200, 50);
		toFileButton.addActionListener(new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				JFileChooser fc = new JFileChooser();
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); 
		    	int i = fc.showDialog(jFrame, "选择导出目录");
		    	if(i == JFileChooser.APPROVE_OPTION) {
					toPath = fc.getSelectedFile().getPath();
					tempFilePath = toPath + "/temp";
					System.out.println(toPath);
		    	}				
			}
		});		
		
		JButton startButton = new javax.swing.JButton();		
		startButton.setText("开始导出");
		startButton.setBounds(550, 10, 200, 50);
		startButton.addActionListener(new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				System.out.println("开始");
				startImportFile();
			}
		});	
		
		
		textArea = new  TextArea("", 20, 43, TextArea.SCROLLBARS_VERTICAL_ONLY);
		textArea.setBounds(10, 70, 700, 350);
		textArea.setEditable(false);
		
		jFrame.setSize(800, 500);
		jFrame.getContentPane().setLayout(null);
		jFrame.add(selectFrom);
		jFrame.add(toFileButton);
		jFrame.add(textArea);
		jFrame.add(startButton);
		jFrame.setResizable(false);
		
		jFrame.setVisible(true);
	}
	
	public static void appendText(String s)
	{
		textArea.append(s + "\n");
	}
	
	public static void startImportFile()
	{
	   ImportFile im = new ImportFile("/home/gzc/Documents/Yunio/littlecube/积木造句/积木造句/文件格式", "/home/gzc/Documents/Yunio/littlecube/积木造句/积木造句/文件生成");
		//ImportFile im = new ImportFile(fromPath,toPath);
		
		im.onStart();
	}
	
}
