package com.app.utils

import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import com.app.R

class SoundManager private constructor() {

    private var playCount = 1

    companion object {
        @Volatile
        private var instance: SoundManager? = null

        fun getInstance(): SoundManager {
            return instance ?: synchronized(this) {
                instance ?: SoundManager().also { instance = it }
            }
        }
    }

    private var mediaPlayer: MediaPlayer? = null
    private var moveLeftMediaPlayer: MediaPlayer? = null
    private var moveRightMediaPlayer: MediaPlayer? = null

    fun init(context: Context) {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(context.applicationContext, R.raw.go_straight)
            mediaPlayer?.setOnCompletionListener {
                printLog("straight listener")

                mediaPlayer?.start()

//                if (playCount < 1) {
//                    playCount++
//                    mediaPlayer?.start()
//                } else {
//                    mediaPlayer?.stop()
//                }
            }

        }

        if (moveLeftMediaPlayer == null) {
            moveLeftMediaPlayer = MediaPlayer.create(context.applicationContext, R.raw.turn_left)
            moveLeftMediaPlayer?.setOnCompletionListener {
                printLog("left listener")

                moveLeftMediaPlayer?.start()
            }

        }

        if (moveRightMediaPlayer == null) {
            moveRightMediaPlayer = MediaPlayer.create(context.applicationContext, R.raw.turn_right)

            moveRightMediaPlayer?.setOnCompletionListener {
                printLog("right listener")

                moveRightMediaPlayer?.start()

            }
        }
    }

    fun playSoundGoStraight() {
        printLog("playSoundGoStraight")
        mediaPlayer?.start()
        moveRightMediaPlayer?.stop()
        moveLeftMediaPlayer?.stop()
    }

    fun stopSound() {
        mediaPlayer?.pause()
    }

    fun stop() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    fun playSoundMoveLeft() {
        printLog("playSoundMoveLeft")

        moveLeftMediaPlayer?.start()
        moveRightMediaPlayer?.stop()
        mediaPlayer?.stop()

    }

    fun playSoundMoveRight() {
        printLog("playSoundMoveRight")
        moveRightMediaPlayer?.start()
        moveLeftMediaPlayer?.stop()
        mediaPlayer?.stop()

    }

    fun printLog(string: String){
        Log.d("SOUND_MANAGER_" , string)
    }

}
