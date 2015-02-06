
#include "client.h"
client::~client()
{
}
static inline QByteArray IntToArray(qint32 source);
client::client() : QObject()
{
socket = new QTcpSocket(this);
}
bool client::connectToHost(QString host)
{
socket->connectToHost(host, 1024);
return socket->waitForConnected();
}
bool client::writeData(QByteArray data)
{
if(socket->state() == QAbstractSocket::ConnectedState)
{
socket->write(IntToArray(data.size())); //write size of data
socket->write(data); //write the data itself
return socket->waitForBytesWritten();
}
else
return false;
}
QByteArray IntToArray(qint32 source) //Use qint32 to ensure that the number have 4 bytes
{
//Avoid use of cast, this is the Qt way to serialize objects
QByteArray temp;
QDataStream data(&temp, QIODevice::ReadWrite);
data << source;
return temp;
}
