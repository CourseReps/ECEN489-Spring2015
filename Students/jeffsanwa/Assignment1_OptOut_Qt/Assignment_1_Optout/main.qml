import QtQuick 2.4
import QtQuick.Window 2.2
import QtLocation 5.3
import QtPositioning 5.2

Window {
    visible: true
    Plugin {
        id: plugin
        preferred: ["nokia", "foo"]

    }

    Map {
        id: map
        plugin: plugin

        zoomLevel: map.minimumZoomLevel

        gesture.enabled: true
    }
}
