package model;




/**
 * @author Ido
 *
 */
public class MainModel implements IModel {

	public static final int NO_OF_PLAYERS = 2;
	private static final int RATE_THRESHOLD = 20;
	public static final int STAGE_WIDTH = 1200;
	public static final int STAGE_HEIGHT = 800;
	
	private static final double PULSE_THRESHOLD = 10 * 1000; // seconds
	private PersonModel[] players;
	private WaterModel waterModel;
	private FlowerModel flowerModel;

	/**
	 * @return the players
	 */
	public PersonModel[] getPlayers() {
		return players;
	}

	/**
	 * 
	 */
	public MainModel() {
		players = new PersonModel[NO_OF_PLAYERS];
		for (int i = 0; i < NO_OF_PLAYERS; i++) {
			players[i] = new PersonModel(i);
		}
		waterModel = new WaterModel();
		flowerModel = new FlowerModel();
	}

	@Override
	public void update() {

		// Make the players closer if the are synced
		if (allPlayersReady()) {
			if (areSynced()) {
				sync();
			} else {
				unsync();
			}
		}

		for (int i = 0; i < NO_OF_PLAYERS; i++) {
			players[i].update();
		}
		
		// keep the water moving...
		waterModel.update();
	}

	public boolean allPlayersReady() {
		for (int i = 0; i < NO_OF_PLAYERS; i++) {
			if (!players[i].isReady()) {
				return false;
			}
		}
		return true;
	}

	public void setPersonPos(int index, int x, int y) {
		switch (index) {
		case 0:
			getPlayers()[index].setCenterX(x - (x / 4));
			getPlayers()[index].setCenterY(y / 2);
			break;
		case 1:
			getPlayers()[index].setCenterX(x / 4);
			getPlayers()[index].setCenterY(y / 2);
			break;
		default:
			break;
		}
	}

	public void sync() {
		int rightPlayerXpos = players[0].getCenterX();
		int leftPlayerXpos = players[1].getCenterX();
		int distance = rightPlayerXpos - leftPlayerXpos;

		if (distance > 0) {
			players[0].setCenterX(--rightPlayerXpos);
			players[1].setCenterX(++leftPlayerXpos);
		}
	}

	public void unsync() {

		int rightPlayerXpos = players[0].getCenterX();
		int leftPlayerXpos = players[1].getCenterX();
		int distance = rightPlayerXpos - leftPlayerXpos;

		if (distance < (int) (0.8 * STAGE_WIDTH)) {
			players[0].setCenterX(++rightPlayerXpos);
			players[1].setCenterX(--leftPlayerXpos);
		}
	}

	public boolean areSynced() {
		int rightPlayerRate = players[0].getHeartRate();
		int leftPlayerRate = players[1].getHeartRate();
		int rateGap = Math.abs(rightPlayerRate - leftPlayerRate);

		double rightPlayerLastBit = players[0].getLastBeat();
		double leftPlayerLastBit = players[1].getLastBeat();
		double pulseGap = Math.abs(rightPlayerLastBit - leftPlayerLastBit);

		if (pulseGap < PULSE_THRESHOLD && rateGap < RATE_THRESHOLD) {
			return true;
		}
		return false;
	}

	public void startPlayer(int identifier) {
		players[identifier].start();
	}

	public WaterModel getWaterModel() {
		return this.waterModel;
	}

	/**
	 * @return the flowerModel
	 */
	public FlowerModel getFlowerModel() {
		return flowerModel;
	}
}
