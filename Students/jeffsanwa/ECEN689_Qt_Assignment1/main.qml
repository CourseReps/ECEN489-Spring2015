import QtQuick 2.4
import QtQuick.Window 2.2

Window {
    visible: true


    Rectangle{
        id: mainForm
        width: parent.width
        height: parent.height


        Text{
            text: "Hello World"
        }

        MouseArea{
            anchors.fill: parent

            onClicked: { Qt.quit();}
        }
    }
}
