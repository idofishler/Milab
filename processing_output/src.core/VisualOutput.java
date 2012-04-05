
import model.MainModel;
import processing.core.PApplet;
import processing.serial.Serial;
import view.MainView;
import controller.IController;
import controller.MainController;


public class VisualOutput extends PApplet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int STAGE_WIDTH = 700;
	public static final int STAGE_HEIGHT = 500;
	
	private static final boolean ARDUINO_INPUT_ON = true;

	IController mainController;
	Serial port;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		PApplet.main(new String[] {  "--present", "VisualOutput" });
	}

	public void setup() {

		if (ARDUINO_INPUT_ON) {
			initSirialPort();
		}

		size(STAGE_WIDTH, STAGE_HEIGHT);

		MainModel mainModel = new MainModel();
		MainView mainView = new MainView(this, mainModel);

		mainController = new MainController(mainModel, mainView);
	}

	private void initSirialPort() {
		try {
			System.out.println("Sirial Port to use: " + Serial.list()[0]);
			port = new Serial(this, Serial.list()[0], 115200);
		}
		catch (Exception e) {
			// TODO: handle exception
			System.err.println(e.getMessage());
			System.exit(1);
		}

	}

	public void draw() {
		if (ARDUINO_INPUT_ON) {
			CheckSerialEvent();			
		}
		mainController.doUI();
	}

	private void CheckSerialEvent() { 
		if (port.available() > 0) {
			byte inChar = (byte) port.read();
			port.clear();
			if (inChar == '0') {
				mainController.event(0);
			}
			if (inChar == '1') {
				mainController.event(1);
			}
		}
	}

	@Override
	public void keyPressed() {
		if (key == '0') {
			mainController.event(0);
		}
		if (key == '1') {
			mainController.event(1);
		}
	}
	
	/* (non-Javadoc)
	 * @see processing.core.PApplet#mouseClicked()
	 */
	@Override
	public void mouseClicked() {
		if (mouseButton == RIGHT) {
			mainController.event(0);
		}
		if (mouseButton == LEFT) {
			mainController.event(1);
		}
	}
	
	
}

