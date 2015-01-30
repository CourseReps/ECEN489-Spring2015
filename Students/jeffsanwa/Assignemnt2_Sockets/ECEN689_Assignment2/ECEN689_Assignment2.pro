TEMPLATE = app

QT += qml quick network core

SOURCES += main.cpp \
    client.cpp \
    server.cpp

RESOURCES += qml.qrc

# Additional import path used to resolve QML modules in Qt Creator's code model
QML_IMPORT_PATH =

# Default rules for deployment.
include(deployment.pri)

HEADERS += \
    client.h \
    server.h
