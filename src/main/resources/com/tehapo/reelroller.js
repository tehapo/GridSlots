window.com_tehapo_ReelRoller = function() {
    "use strict";

    this.gridElem = this.getElement(this.getParentId());

    this.roll = function(row, duration) {
        var scrollerElem = this.gridElem.getElementsByClassName("v-grid-scroller-vertical")[0];
        var rowHeight = this.gridElem.getElementsByClassName("v-grid-row")[0].clientHeight;

        var initialScrollTop = scrollerElem.scrollTop;
        var targetScrollTop = (row * rowHeight) - (this.gridElem.clientHeight / 2 - rowHeight / 2); // Middle.

        var delta = targetScrollTop - initialScrollTop;
        var startTime = null;
        var completionCallback = this.onAnimationComplete;

        function animate(timestamp) {
            if (startTime === null) {
                startTime = timestamp;
            }

            var progress = Math.min(1, (timestamp - startTime) / duration);
            scrollerElem.scrollTop = initialScrollTop + progress * delta;
            if (progress < 1) {
                window.requestAnimationFrame(animate);
            } else {
                soundFx.playSound("stop.wav");
                completionCallback();
            }
        }
        animate.bind(this);
        window.requestAnimationFrame(animate);
    };
};
