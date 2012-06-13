package model;

import java.util.ArrayList;

import utils.MP3Player;

public class SoundModel implements IModel {

	private static final int INIT_NUM_OF_CHANNELS = 0;

	private static final String HEART_BIT_SOUND_PATH = "resource/HeartBit_strong.mp3";
	private static final String HEART_BIT_SOUND_PATH_L = "resource/HeartBit_L.mp3";
	private static final String HEART_BIT_SOUND_PATH_R = "resource/HeartBit_R.mp3";

	private static final String[] CHNNEL_PATHS_0 = { 
		"resource/channel0.mp3",
		"resource/channel1.mp3",
		"resource/channel2.mp3",
		"resource/channel3.mp3",
		"resource/channel4.mp3",	
	};
	private static final String[] CHNNEL_PATHS_1 = { 
		"resource/channel1_1.mp3",
		"resource/channel0_1.mp3",
		"resource/channel2_1.mp3",
		"resource/channel3_1.mp3",
		"resource/channel4_1.mp3",	
	};
	private static final String[] CHNNEL_PATHS_2 = { 
		"resource/channel0_2.mp3",
		"resource/channel1_2.mp3",
		"resource/channel2_2.mp3",
		"resource/channel3_2.mp3",
		"resource/channel4_2.mp3",
		"resource/channel5_2.mp3",
		"resource/channel6_2.mp3",
	};
	
	private static final String[] CHNNEL_PATHS = CHNNEL_PATHS_2;
	private static final String GATE_KEEPER_SONG_PATH = "resource/01 Gatekeeper.mp3";

	private static final int STOP_PULSE_TH = 1;

	private static final boolean ASAF_SONG = CHNNEL_PATHS == CHNNEL_PATHS_2;

	private int numOfChannels;
	private int prevNumOfChannels;
	private boolean playHeartBitSound;

	//private MP3Player heartBitSound;
	private MP3Player heartBitSound_L;
	private MP3Player heartBitSound_R;
	private ArrayList<MP3Player> channelPlayers;

//	private MP3Player song;
//	private int gain = 0;



	public SoundModel() {
		heartBitSound_L = new MP3Player(HEART_BIT_SOUND_PATH_L);
		heartBitSound_R = new MP3Player(HEART_BIT_SOUND_PATH_R);
		channelPlayers = new ArrayList<MP3Player>();
		
//		song = new MP3Player(GATE_KEEPER_SONG_PATH);
//		song.play();
//		song.setVolume(0);
	}

	public void init() {
		stop();
		channelPlayers.clear();
		numOfChannels = INIT_NUM_OF_CHANNELS;
		prevNumOfChannels = numOfChannels;
		playHeartBitSound = true;
		loadAllChannelsAtOnce();
	}

	private void loadAllChannelsAtOnce() {
		for (String channelName : CHNNEL_PATHS) {
			MP3Player channel = new MP3Player(channelName);
			channel.play();
			channel.mute();				
			channelPlayers.add(channel);
		}
	}

	@Override
	public void update() {
		if (prevNumOfChannels < numOfChannels) {
			MP3Player channel = channelPlayers.get(prevNumOfChannels);
			if (ASAF_SONG) {
				if (prevNumOfChannels > 0) {					
					MP3Player oldChannel = channelPlayers.get(prevNumOfChannels-1);
					oldChannel.fadeOut();
				}
			}
			channel.fadeIn();
			prevNumOfChannels++;
		} else if (prevNumOfChannels > numOfChannels) {
			MP3Player channel = channelPlayers.get(numOfChannels);
			channel.fadeOut();
			if (ASAF_SONG) {
				if (numOfChannels > 0) {
					MP3Player newChannel = channelPlayers.get(numOfChannels-1);
					newChannel.fadeIn();
				}
			}
			prevNumOfChannels--;
		}
		if (numOfChannels > STOP_PULSE_TH) {
			playHeartBitSound = false;
		} else {
			playHeartBitSound = true;
		}
	}

	// Play hear bit sound for each event - in separate channels for different players
	// Left player - gets the left channel, Right gets the right.
	public void playPulse(int identifier) {
		if (playHeartBitSound) {
			if (identifier == 0) {
				heartBitSound_R.play();
			} else if (identifier == 1) {
				heartBitSound_L.play();
			}			
		}
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
	
//	public void volumeUp() {
//		gain += 1;
//		song.setVolume(gain);
//	}
//	
//	public void volumeDown() {
//		gain -= 1;
//		song.setVolume(gain);
//	}
}
