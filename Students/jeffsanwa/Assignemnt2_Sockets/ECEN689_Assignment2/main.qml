import QtQuick 2.4
import QtQuick.Window 2.2
import Server 1.0
import Clients 1.0

Window {
    id: root
    visible: true
    width: 600
    height: 600

    property TCPClient myClient

    TCPServer{
        id: myServer
    }

    function printServerData(data){
        serverText.text = "Server Data "+data;
    }

    Rectangle{
        id: wrapper
        width: 200
        height: 200
        color: "blue"

        TextInput{
            id: clientInput
            width: 100
            height: 50
            anchors.centerIn: parent
            color:"white"
            text: "Input Here"
        }
    }


    Rectangle{
        id: clientButton
        anchors.centerIn: parent
        width: 100
        height: 100
        color: "red"

        Text{
            id: connectT
            text: "Connect"
            color: "white"
        }

        MouseArea{
            id: clickableArea
            anchors.fill: parent

            onClicked: {
                myClient  = Qt.createQmlObject("import QtQuick 2.4;import Clients 1.0; TCPClient{ id: myClient }",root);
                myClient.connectToHost("127.0.0.1");
                myClient.writeData("Hello");
            }
        }
    }

    Rectangle{
        id: sendFromClient
        width: 100
        height: 100
        color: "green"
        anchors.top: clientButton.bottom

        Text{
            id: sendData
            text: "Send"
            color: "white"
        }
        MouseArea{
            id:click
            anchors.fill: parent

            onClicked: {
                myClient.writeData(clientInput.text);
            }
        }
    }

    Text{
        id: serverText
        text: "Server Text: "
        width: 100
        height: 100
        x: 0
        y: 200
    }

    Component.onCompleted: {
        myServer.dataReceived.connect(printServerData);
    }
}
