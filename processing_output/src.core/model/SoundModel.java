package model;

import java.util.ArrayList;

import utils.MP3Player;
import utils.MyAdvancePlayer;

public class SoundModel implements IModel {

	private static final String HEART_BIT_SOUND_PATH = "resource/HeartBit_strong.mp3";

	private static final int INIT_NUM_OF_CHANNELS = 0;
	private static final String[] CHNNEL_PATHS = { 
		"resource/channel0.mp3",
		"resource/channel1.mp3",
		"resource/channel2.mp3",
		"resource/channel3.mp3",
		"resource/channel4.mp3",	
	};
	private static final String GATE_KEEPER_SONG_PATH = "resource/01 Gatekeeper.mp3";

	private int numOfChannels;
	private int prevNumOfChannels;

	private MP3Player heartBitSound;
	private ArrayList<MP3Player> channelPlayers;



	public SoundModel() {
		heartBitSound = new MP3Player(HEART_BIT_SOUND_PATH);
		channelPlayers = new ArrayList<MP3Player>();
		init();
	}

	public void init() {
		stop();
		channelPlayers.clear();
		numOfChannels = INIT_NUM_OF_CHANNELS;
		prevNumOfChannels = numOfChannels;
	}

	@Override
	public void update() {
		if (prevNumOfChannels < numOfChannels) {
			MP3Player channel = new MP3Player(CHNNEL_PATHS[prevNumOfChannels]);
			channel.play();
			channelPlayers.add(channel);
		} else if (prevNumOfChannels > numOfChannels) {
			MP3Player channel = channelPlayers.remove(numOfChannels);
			channel.stop();
		}
		prevNumOfChannels = numOfChannels;
	}

	// Play hear bit sound for each event
	public void playPulse() {
		heartBitSound.play();

	}

	public void playSongChannels(int distance) {
		int maxDistance = (int) (MainModel.STAGE_WIDTH * MainModel.MAX_DISTANCE_FACTOR);
		float closeness =  (float) distance / (float) maxDistance;
		numOfChannels = (int) ((1 - closeness) * CHNNEL_PATHS.length);
	}
	
	public void stop() {
		for (MP3Player channel : channelPlayers) {
			if (channel.isRunning()) {
				channel.stop();
			}
		}
	}
}
