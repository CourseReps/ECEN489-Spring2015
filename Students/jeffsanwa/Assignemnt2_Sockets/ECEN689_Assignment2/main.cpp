#include <QGuiApplication>
#include <QQmlApplicationEngine>
#include <QQmlContext>
#include <QQuickView>
#include <QQuickItem>

#include "client.h"
#include "server.h"

int main(int argc, char *argv[])
{
    QGuiApplication app(argc, argv);

    QQmlApplicationEngine engine;



    qmlRegisterType<Server>("Server",1,0,"TCPServer");
    qmlRegisterType<client>("Clients",1,0,"TCPClient");

    engine.load(QUrl(QStringLiteral("qrc:/main.qml")));



    return app.exec();
}
