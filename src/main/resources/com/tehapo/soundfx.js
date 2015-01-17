var soundFx = {
    audioContext: null,
    sounds: {},

    loadSound: function(name) {
        if (!this.sounds[name]) {
            var request = new XMLHttpRequest();
            request.open("GET", "VAADIN/sounds/" + name, true);
            request.responseType = "arraybuffer";
            request.onload = function() {
                if (request.status == 200) {
                    this.audioContext.decodeAudioData(request.response, function(buffer) {
                        this.sounds[name] = buffer;
                    }.bind(this));
                }
            }.bind(this);
            request.send();
        }
    },

    playSound: function(name) {
        if (this.sounds[name] && this.audioContext !== null) {
            var source = this.audioContext.createBufferSource();
            source.buffer = this.sounds[name];
            source.connect(this.audioContext.destination);
            source.start();
        }
    },

    init: function() {
        try {
            window.AudioContext = window.AudioContext || window.webkitAudioContext;
            this.audioContext = new AudioContext();
            this.loadSound("stop.wav");
            this.loadSound("winner.wav");

            // Workaround for iOS requiring user interaction before enabling Web Audio.
            if (navigator.userAgent.match(/(iPad|iPhone|iPod)/i)) {
                this.workaround = function() {
                    this.playSound("stop.wav");
                    document.body.removeEventListener("touchstart", this.workaround);
                }.bind(this);
                document.body.addEventListener("touchstart", this.workaround);
            }
        } catch (e) {
            console.log("Web Audio not supported.");
        }
    }
};
soundFx.init();

window.com_tehapo_SoundFx = function() {
    "use strict";

    this.playSound = function(name) {
        soundFx.playSound(name);
    };
};

