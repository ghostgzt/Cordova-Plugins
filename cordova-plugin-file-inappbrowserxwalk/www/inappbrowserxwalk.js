cordova.define("com.gentle.exview.plugins.inAppBrowserXwalk", function(require, exports, module) {
/*global cordova, module*/

function InAppBrowserXwalk() {
 
}

var callbacks = new Array ();

InAppBrowserXwalk.prototype = {
    close: function () {
        cordova.exec(null, null, "InAppBrowserXwalk", "close", []);
    },
    addEventListener: function (eventname, func) {
        callbacks[eventname] = func;
    },
    removeEventListener: function (eventname) {
        callbacks[eventname] = undefined;
    },
    show: function () {
        cordova.exec(null, null, "InAppBrowserXwalk", "show", []);
    },
    hide: function () {
        cordova.exec(null, null, "InAppBrowserXwalk", "hide", []);
    },
        executeScript: function(injectDetails, cb) {
            if (injectDetails.code) {
                cordova.exec(cb, null, "InAppBrowserXwalk", "injectScriptCode", [injectDetails.code, !!cb]);
            } else if (injectDetails.file) {
                cordova.exec(cb, null, "InAppBrowserXwalk", "injectScriptFile", [injectDetails.file, !!cb]);
            } else {
                throw new Error('executeScript requires exactly one of code or file to be specified');
            }
        },

        insertCSS: function(injectDetails, cb) {
            if (injectDetails.code) {
                 cordova.exec(cb, null, "InAppBrowserXwalk", "injectStyleCode", [injectDetails.code, !!cb]);
            } else if (injectDetails.file) {
                 cordova.exec(cb, null, "InAppBrowserXwalk", "injectStyleFile", [injectDetails.file, !!cb]);
            } else {
                throw new Error('insertCSS requires exactly one of code or file to be specified');
            }
        }
}

var callback = function(event) {
    if (event.type === "loadstart" && callbacks['loadstart'] !== undefined) {
        callbacks['loadstart'](event);
    }
    if (event.type === "loadstop" && callbacks['loadstop'] !== undefined) {
        callbacks['loadstop'](event);
    }
    if (event.type === "loaderror" && callbacks['loaderror'] !== undefined) {
        callbacks['loaderror'](event);
    }
    if (event.type === "loadprogess" && callbacks['loadprogess'] !== undefined) {
        callbacks['loadprogess'](event);
    }	
    if (event.type === "exit" && callbacks['exit'] !== undefined) {
        callbacks['exit']();
    }
}

module.exports = {
    open: function (url, options) {
        options = (options === undefined) ? "{}" : JSON.stringify(options);
        cordova.exec(callback, null, "InAppBrowserXwalk", "open", [url, options]);
        return new InAppBrowserXwalk();
    }
};

});
