#ifndef CLIENT_H
#define CLIENT_H

#include <QObject>
#include <QTcpSocket>

class client : public QObject
{
    Q_OBJECT
public:
    client();
    ~client();

signals:

public slots:

public slots:
    bool connectToHost(QString host);
    bool writeData(QByteArray data);

private:
    QTcpSocket *socket;
};

#endif // CLIENT_H
