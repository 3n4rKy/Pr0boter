package piclient;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.swt.internal.win32.OS;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import com.util.CommandPrepare;
import com.util.PacketSender;

public class Menu {
	protected Shell shell;
	static int FORWARD_KEY = 0x057;
	static int BACKWARD_KEY = 0x053;
	static int LEFT_KEY = 0x041;
	static int RIGHT_KEY = 0x044;
	static int HIGH_KEY = 0x8000;
	private Text text;
	InetAddress ipAddress;

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Menu window = new Menu();
		try {
			window.open();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.exit(0);
	}

	/**
	 * Open the window.
	 * @throws UnknownHostException 
	 */
	public void open() throws UnknownHostException {
		final Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		final CommandPrepare cp = new CommandPrepare();
		final PacketSender ps = new PacketSender();
		Timer timer = new Timer();
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				boolean bForwared = (OS.GetKeyState(FORWARD_KEY) & HIGH_KEY) == 0;
				boolean bBackward = (OS.GetKeyState(BACKWARD_KEY) & HIGH_KEY) == 0;
				boolean bLeft = (OS.GetKeyState(LEFT_KEY) & HIGH_KEY) == 0;
				boolean bRight = (OS.GetKeyState(RIGHT_KEY) & HIGH_KEY) == 0;
				String command = cp.setCommand(!bForwared, !bBackward, !bLeft, !bRight);
				try {
					ps.sendPacket(ipAddress, command, false);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//System.out.println(command);
			}
		};
		timer.schedule(task, 0, 100);
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(450, 300);
		shell.setText("SWT Application");
		
		text = new Text(shell, SWT.BORDER);
		text.setText("127.0.0.1");
		text.setBounds(10, 22, 76, 21);
		try {
			ipAddress = InetAddress.getByName(text.getText().toString());
		} catch (UnknownHostException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		Button btnSetIp = new Button(shell, SWT.NONE);
		btnSetIp.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					ipAddress = InetAddress.getByName(text.getText().toString());
				} catch (UnknownHostException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnSetIp.setBounds(98, 18, 75, 25);
		btnSetIp.setText("Set IP");

	}
}

