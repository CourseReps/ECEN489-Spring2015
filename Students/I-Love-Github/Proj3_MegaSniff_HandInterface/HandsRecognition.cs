using System.Text;
using System.Net;
using System.Net.Sockets;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Drawing;
using System.Drawing.Imaging;
using System.Linq;

namespace hands_viewer.cs
{
    class HandsRecognition
    {
        String IPString = "10.202.113.62";

        byte[] LUT;
        private int[] valueShift = new int[10];
        private MainForm form;
        private bool _disconnected = false;
        //Queue containing depth image - for synchronization purposes
        private Queue<PXCMImage> m_images;
        private const int NumberOfFramesToDelay = 3;
        private int _framesCounter = 0;
        private float _maxRange;
       


        public HandsRecognition(MainForm form)
        {
            m_images = new Queue<PXCMImage>();
            this.form = form;
            LUT = Enumerable.Repeat((byte)0, 256).ToArray();
            LUT[255] = 1;

            StartSocket();
        }

        /* Checking if sensor device connect or not */
        private bool DisplayDeviceConnection(bool state)
        {
            if (state)
            {
                if (!_disconnected) form.UpdateStatus("Device Disconnected");
                _disconnected = true;
            }
            else
            {
                if (_disconnected) form.UpdateStatus("Device Reconnected");
                _disconnected = false;
            }
            return _disconnected;
        }

        /* Displaying current frame gestures */
        private int DisplayGesture(PXCMHandData handAnalysis)
        {
            int firedGesturesNumber = handAnalysis.QueryFiredGesturesNumber();
            string gestureStatusLeft = string.Empty;
            string gestureStatusRight = string.Empty;
            if (firedGesturesNumber == 0)
            {
                return -1;
            }
           
            for (int i = 0; i < firedGesturesNumber; i++)
            {
                PXCMHandData.GestureData gestureData;
                if (handAnalysis.QueryFiredGestureData(i, out gestureData) == pxcmStatus.PXCM_STATUS_NO_ERROR)
                {
                    PXCMHandData.IHand handData;
                    if (handAnalysis.QueryHandDataById(gestureData.handId, out handData) != pxcmStatus.PXCM_STATUS_NO_ERROR)
                        return -1;
                   
                    PXCMHandData.BodySideType bodySideType = handData.QueryBodySide();
                    if (bodySideType == PXCMHandData.BodySideType.BODY_SIDE_LEFT)
                    {
                        gestureStatusLeft += gestureData.name;
                    }
                    else if (bodySideType == PXCMHandData.BodySideType.BODY_SIDE_RIGHT)
                    {
                        gestureStatusRight += gestureData.name;
                    }

                   
                }
                  
            }

            int value = -1;
            if (gestureStatusLeft == "spreadfingers" && gestureStatusRight == "spreadfingers")
            {
                value = 0;
            }
            else if (gestureStatusLeft == "spreadfingers" && gestureStatusRight == "fist")
            {
                value = 1;
            }
            else if (gestureStatusLeft == "fist" && gestureStatusRight == "spreadfingers")
            {
                value = 2;
            }
            else if (gestureStatusLeft == "fist" && gestureStatusRight == "fist")
            {
                value = 3;
            }

            if (value != -1)
            {
                //form.UpdateInfo("Gesture: " + value + "\n", Color.SeaGreen);
                return value;
            }

            return -1;
        }

        private int DisplayHandOrientation(PXCMHandData handAnalysis)
        {
            // HAND ORIENTATION
            PXCMHandData handOutput = handAnalysis;
            int numOfHands = handOutput.QueryNumberOfHands();
            int value = -1;

            if (numOfHands != 2)
                return -1;

            if (numOfHands == 2)
            {
                PXCMHandData.IHand hand1Data;
                PXCMHandData.IHand hand2Data;
                if (handOutput.QueryHandData(PXCMHandData.AccessOrderType.ACCESS_ORDER_BY_TIME, 0, out hand1Data) == pxcmStatus.PXCM_STATUS_NO_ERROR &&
                handOutput.QueryHandData(PXCMHandData.AccessOrderType.ACCESS_ORDER_BY_TIME, 1, out hand2Data) == pxcmStatus.PXCM_STATUS_NO_ERROR)
                {
                    PXCMHandData.JointData[] joint1Data = new PXCMHandData.JointData[3];
                    PXCMHandData.JointData[] joint2Data = new PXCMHandData.JointData[3];
                    hand1Data.QueryTrackedJoint((PXCMHandData.JointType)0, out joint1Data[0]);
                    hand1Data.QueryTrackedJoint((PXCMHandData.JointType)1, out joint1Data[1]);
                    hand1Data.QueryTrackedJoint((PXCMHandData.JointType)10, out joint1Data[2]);
                    hand2Data.QueryTrackedJoint((PXCMHandData.JointType)0, out joint2Data[0]);
                    hand2Data.QueryTrackedJoint((PXCMHandData.JointType)1, out joint2Data[1]);
                    hand2Data.QueryTrackedJoint((PXCMHandData.JointType)10, out joint2Data[2]);

                    Single[] x1 = {
                        joint1Data[0].globalOrientation.x,
                        joint1Data[1].globalOrientation.x,
                        joint1Data[2].globalOrientation.x
                   };

                    Single[] x2 = {
                        joint2Data[0].globalOrientation.x,
                        joint2Data[1].globalOrientation.x,
                        joint2Data[2].globalOrientation.x
                    };

                    form.updateXlabel(form.label1, x1[0].ToString());
                    form.updateXlabel(form.label2, x1[1].ToString());
                    form.updateXlabel(form.label3, x1[2].ToString());
                    form.updateXlabel(form.label6, x2[0].ToString());
                    form.updateXlabel(form.label7, x2[1].ToString());
                    form.updateXlabel(form.label8, x2[2].ToString());

                    int x1_pos = -1;
                    int x2_pos = -1;

                    if (x1[0] > 0.9 && x1[1] > 0.9 && x1[2] > 0.9)
                    {
                        x1_pos = 1;
                    }
                    else if (x1[0] < 0.85 && x1[1] < 0.85 && x1[2] < 0.85 )
                    {
                        x1_pos = 2;
                    }

                    if (x2[0] > 0.9 && x2[1] > 0.9 && x2[2] > 0.9)
                    {
                        x2_pos = 1;
                    }
                    else if (x2[0] < 0.85 && x2[1] < 0.85 && x2[2] < 0.85 )
                    {
                        x2_pos = 2;
                    }

                    if (x1_pos == 1 && x2_pos == 1)
                    {
                        value = 0;
                    }
                    else if (x1_pos == 2 && x2_pos == 1)
                    {
                        value = 1;
                    }
                    else if (x1_pos == 1 && x2_pos == 2)
                    {
                        value = 2;
                    }
                    else if (x1_pos == 2 && x2_pos == 2)
                    {
                        value = 3;
                    }

                    form.updateXlabel(form.label4, x1_pos.ToString());
                    form.updateXlabel(form.label5, x2_pos.ToString());
                    form.updateXlabel(form.label9, value.ToString());

                    if (value != -1)
                    {
                        //form.UpdateInfo("Orientation: " + value + "\n", Color.SeaGreen);
                        return value;
                    }

                }
            }
            
            return -1;
        }

        /* Displaying Depth/Mask Images - for depth image only we use a delay of NumberOfFramesToDelay to sync image with tracking */
        private unsafe void DisplayPicture(PXCMImage depth, PXCMHandData handAnalysis)
        {
            if (depth == null)
                return;

            PXCMImage image = depth;

            //Mask Image
            //if (form.GetLabelmapState())
            //{
            //    Bitmap labeledBitmap = null;
            //    try
            //    {
            //        labeledBitmap = new Bitmap(image.info.width, image.info.height, PixelFormat.Format32bppRgb);
            //        for (int j = 0; j < handAnalysis.QueryNumberOfHands(); j++)
            //        {
            //            int id;
            //            PXCMImage.ImageData data;

            //            handAnalysis.QueryHandId(PXCMHandData.AccessOrderType.ACCESS_ORDER_BY_TIME, j, out id);
            //            //Get hand by time of appearance
            //            PXCMHandData.IHand handData;
            //            handAnalysis.QueryHandData(PXCMHandData.AccessOrderType.ACCESS_ORDER_BY_TIME, j, out handData);
            //            if (handData != null &&
            //                (handData.QuerySegmentationImage(out image) >= pxcmStatus.PXCM_STATUS_NO_ERROR))
            //            {
            //                if (image.AcquireAccess(PXCMImage.Access.ACCESS_READ, PXCMImage.PixelFormat.PIXEL_FORMAT_Y8,
            //                    out data) >= pxcmStatus.PXCM_STATUS_NO_ERROR)
            //                {
            //                    Rectangle rect = new Rectangle(0, 0, image.info.width, image.info.height);

            //                    BitmapData bitmapdata = labeledBitmap.LockBits(rect, ImageLockMode.ReadWrite,
            //                        labeledBitmap.PixelFormat);
            //                    byte* numPtr = (byte*) bitmapdata.Scan0; //dst
            //                    byte* numPtr2 = (byte*) data.planes[0]; //row
            //                    int imagesize = image.info.width*image.info.height;
            //                    byte num2 = (byte) handData.QueryBodySide();

            //                    byte tmp = 0;
            //                    for (int i = 0; i < imagesize; i++, numPtr += 4, numPtr2++)
            //                    {
            //                        tmp = (byte) (LUT[numPtr2[0]]*num2*100);
            //                        numPtr[0] = (Byte) (tmp | numPtr[0]);
            //                        numPtr[1] = (Byte) (tmp | numPtr[1]);
            //                        numPtr[2] = (Byte) (tmp | numPtr[2]);
            //                        numPtr[3] = 0xff;
            //                    }

            //                    labeledBitmap.UnlockBits(bitmapdata);
            //                    image.ReleaseAccess(data);
                                
            //                }
            //            }
            //        }
            //        if (labeledBitmap != null)
            //        {
            //            form.DisplayBitmap(labeledBitmap);
            //            labeledBitmap.Dispose();
            //        }
            //        image.Dispose();
            //    }
            //    catch (Exception)
            //    {
            //        if (labeledBitmap != null)
            //        {
            //            labeledBitmap.Dispose();
            //        }
            //        if (image != null)
            //        {
            //            image.Dispose();
            //        }
            //    }

            //}//end label image

            //Depth Image
            //else
            //{
                //collecting 3 images inside a queue and displaying the oldest image
                PXCMImage.ImageInfo info;
                PXCMImage image2;

                info = image.QueryInfo();
                image2 = form.g_session.CreateImage(info);
                image2.CopyImage(image);
                m_images.Enqueue(image2);
                if (m_images.Count == NumberOfFramesToDelay)
                {
                    Bitmap depthBitmap;
                    try
                    {
                        depthBitmap = new Bitmap(image.info.width, image.info.height, PixelFormat.Format32bppRgb);
                    }
                    catch (Exception)
                    {
                        image.Dispose();
                        PXCMImage queImage = m_images.Dequeue();
                        queImage.Dispose();
                        return;
                    }

                    PXCMImage.ImageData data3;
                    PXCMImage image3 = m_images.Dequeue();
                    if (image3.AcquireAccess(PXCMImage.Access.ACCESS_READ, PXCMImage.PixelFormat.PIXEL_FORMAT_DEPTH, out data3) >= pxcmStatus.PXCM_STATUS_NO_ERROR)
                    {
                        float fMaxValue = _maxRange;
                        byte cVal;

                        Rectangle rect = new Rectangle(0, 0, image.info.width, image.info.height);
                        BitmapData bitmapdata = depthBitmap.LockBits(rect, ImageLockMode.ReadWrite, depthBitmap.PixelFormat);

                        byte* pDst = (byte*)bitmapdata.Scan0;
                        short* pSrc = (short*)data3.planes[0];
                        int size = image.info.width * image.info.height;

                        for (int i = 0; i < size; i++, pSrc++, pDst += 4)
                        {
                            cVal = (byte)((*pSrc) / fMaxValue * 255);
                            if (cVal != 0)
                                cVal = (byte)(255 - cVal);
                           
                            pDst[0] = cVal;
                            pDst[1] = cVal;
                            pDst[2] = cVal;
                            pDst[3] = 255;
                        }
                        try
                        {
                            depthBitmap.UnlockBits(bitmapdata);
                        }
                        catch (Exception)
                        {
                            image3.ReleaseAccess(data3);
                            depthBitmap.Dispose();
                            image3.Dispose();
                            return;
                        }

                        form.DisplayBitmap(depthBitmap);
                        image3.ReleaseAccess(data3);
                    }
                    depthBitmap.Dispose();
                    image3.Dispose();
                }
            //}
        }



        /* Displaying current frames hand joints */
        private void DisplayJoints(PXCMHandData handOutput, long timeStamp = 0)
        {
            //if (form.GetJointsState() || form.GetSkeletonState())
            //{
            
            //Iterate hands
                PXCMHandData.JointData[][] nodes = new PXCMHandData.JointData[][] { new PXCMHandData.JointData[0x20], new PXCMHandData.JointData[0x20] };
                int numOfHands = handOutput.QueryNumberOfHands();
                for (int i = 0; i < numOfHands; i++)
                {
                    //Get hand by time of appearence
                    //PXCMHandAnalysis.HandData handData = new PXCMHandAnalysis.HandData();
                    String jointString = String.Empty;
                    
                    PXCMHandData.IHand handData;
                    if (handOutput.QueryHandData(PXCMHandData.AccessOrderType.ACCESS_ORDER_BY_TIME, i, out handData) == pxcmStatus.PXCM_STATUS_NO_ERROR)
                    {
                        if (handData != null)
                        {
                            //Iterate Joints
                            for (int j = 0; j < 0x20; j++)
                            {
                                PXCMHandData.JointData jointData;
                                handData.QueryTrackedJoint((PXCMHandData.JointType)j, out jointData);
                                nodes[i][j] = jointData;
                            } // end iterating over joints

                            //for (int j = 0; j < 22; j++)
                            //{
                            //    Single x, y, z, w;
                            //    x = nodes[i][j].localRotation.x;
                            //    y = nodes[i][j].localRotation.y;
                            //    z = nodes[i][j].localRotation.z;
                            //    w = nodes[i][j].localRotation.w;
                            //    jointString = i + "x" + j + ", " + x + "," + y + "," + z + "," + w +"\n";
                            //    form.UpdateInfo(jointString, Color.Red);
                            //} // end iterating over joints
                        }
                    }
                } // end itrating over hands

                form.DisplayJoints(nodes, numOfHands);
            //}
            //else
            //{
            //    form.DisplayJoints(null, 0);
            //}
        }

        /* Displaying current frame alerts */
        private void DisplayAlerts(PXCMHandData handAnalysis, int frameNumber)
        {
            bool isChanged = false;
            string sAlert = "Alert: ";
            for (int i = 0; i < handAnalysis.QueryFiredAlertsNumber(); i++)
            {
                PXCMHandData.AlertData alertData;
                if (handAnalysis.QueryFiredAlertData(i, out alertData) != pxcmStatus.PXCM_STATUS_NO_ERROR)
                    continue;

                //See PXCMHandAnalysis.AlertData.AlertType for all available alerts
                switch (alertData.label)
                {
                    case PXCMHandData.AlertType.ALERT_HAND_DETECTED:
                        {

                            sAlert += "Hand Detected, ";
                            isChanged = true;
                            break;
                        }
                    case PXCMHandData.AlertType.ALERT_HAND_NOT_DETECTED:
                        {

                            sAlert += "Hand Not Detected, ";
                            isChanged = true;
                            break;
                        }
                    case PXCMHandData.AlertType.ALERT_HAND_CALIBRATED:
                        {

                            sAlert += "Hand Calibrated, ";
                            isChanged = true;
                            break;
                        }
                    case PXCMHandData.AlertType.ALERT_HAND_NOT_CALIBRATED:
                        {

                            sAlert += "Hand Not Calibrated, ";
                            isChanged = true;
                            break;
                        }
                    case PXCMHandData.AlertType.ALERT_HAND_INSIDE_BORDERS:
                        {

                            sAlert += "Hand Inside Border, ";
                            isChanged = true;
                            break;
                        }
                    case PXCMHandData.AlertType.ALERT_HAND_OUT_OF_BORDERS:
                        {

                            sAlert += "Hand Out Of Borders, ";
                            isChanged = true;
                            break;
                        }
                }
            }
            if (isChanged)
            {
                form.UpdateInfo("Frame " + frameNumber + ") " + sAlert + "\n", Color.RoyalBlue);
            }


        }

        public static pxcmStatus OnNewFrame(Int32 mid, PXCMBase module, PXCMCapture.Sample sample)
        {
            return pxcmStatus.PXCM_STATUS_NO_ERROR;
        }

      



        /* Using PXCMSenseManager to handle data */
        public void SimplePipeline()
        {   
            form.UpdateInfo(String.Empty,Color.Black);
            bool liveCamera = false;

            bool flag = true;
            PXCMSenseManager instance = null;
            _disconnected = false;
            instance = form.g_session.CreateSenseManager();
            if (instance == null)
            {
                form.UpdateStatus("Failed creating SenseManager");
                return;
            }

            if (form.GetRecordState())
            {
                instance.captureManager.SetFileName(form.GetFileName(), true);
                PXCMCapture.DeviceInfo info;
                if (form.Devices.TryGetValue(form.GetCheckedDevice(), out info))
                {
                    instance.captureManager.FilterByDeviceInfo(info);
                }

            }
            else if (form.GetPlaybackState())
            {
                instance.captureManager.SetFileName(form.GetFileName(), false);
            }
            else
            {
                PXCMCapture.DeviceInfo info;
                if (String.IsNullOrEmpty(form.GetCheckedDevice()))
                {
                    form.UpdateStatus("Device Failure");
                    return;
                }

                if (form.Devices.TryGetValue(form.GetCheckedDevice(), out info))
                {
                    instance.captureManager.FilterByDeviceInfo(info);
                }

                liveCamera = true;
            }
            /* Set Module */
            pxcmStatus status = instance.EnableHand(form.GetCheckedModule());
            PXCMHandModule handAnalysis = instance.QueryHand();

            if (status != pxcmStatus.PXCM_STATUS_NO_ERROR || handAnalysis == null)
            {
                form.UpdateStatus("Failed Loading Module");
                return;
            }

            PXCMSenseManager.Handler handler = new PXCMSenseManager.Handler();
            handler.onModuleProcessedFrame = new PXCMSenseManager.Handler.OnModuleProcessedFrameDelegate(OnNewFrame);


            PXCMHandConfiguration handConfiguration = handAnalysis.CreateActiveConfiguration();
            PXCMHandData handData = handAnalysis.CreateOutput();

            if (handConfiguration == null)
            {
                form.UpdateStatus("Failed Create Configuration");
                return;
            }
            if (handData==null)
            {
                form.UpdateStatus("Failed Create Output");
                return;
            }

            if (form.getInitGesturesFirstTime() == false)
            {
                int totalNumOfGestures = handConfiguration.QueryGesturesTotalNumber();
                if (totalNumOfGestures > 0)
                {
                    //this.form.UpdateGesturesToList("", 0);
                    //for (int i = 0; i < totalNumOfGestures; i++)
                    //{
                    //    string gestureName = string.Empty;
                    //    if (handConfiguration.QueryGestureNameByIndex(i, out gestureName) ==
                    //        pxcmStatus.PXCM_STATUS_NO_ERROR)
                    //    {
                    //        this.form.UpdateGesturesToList(gestureName, i + 1);                           
                    //    }
                    //}
                  
                    //form.setInitGesturesFirstTime(true);
                    //form.UpdateGesturesListSize();
                }
            }


            FPSTimer timer = new FPSTimer(form);
            form.UpdateStatus("Init Started");
            if (handAnalysis != null && instance.Init(handler) == pxcmStatus.PXCM_STATUS_NO_ERROR)
            {

                PXCMCapture.DeviceInfo dinfo;

                PXCMCapture.Device device = instance.captureManager.device;
                if (device != null)
                {
                    device.QueryDeviceInfo(out dinfo);
                    _maxRange = device.QueryDepthSensorRange().max;

                }
               

                if (handConfiguration != null)
                {
                    handConfiguration.EnableAllAlerts();
                    handConfiguration.EnableSegmentationImage(true);
                    handConfiguration.ApplyChanges();
                    handConfiguration.Update();
                }
                form.UpdateStatus("Streaming");
                int frameCounter = 0;
                int frameNumber = 0;

                handConfiguration.EnableGesture("fist", true);
                handConfiguration.EnableGesture("spreadfingers", true);
                handConfiguration.EnableGesture("wave", true);
                handConfiguration.ApplyChanges();

                while (!form.stop)
                {

                    //string gestureName = form.GetGestureName();
                    //if (string.IsNullOrEmpty(gestureName) == false)
                    //{
                    //    if (handConfiguration.IsGestureEnabled(gestureName) == false)
                    //    {
                    //        handConfiguration.DisableAllGestures();
                    //        handConfiguration.EnableGesture(gestureName, true);
                    //        handConfiguration.ApplyChanges();
                    //    }
                    //}
                    //else
                    //{
                    //    handConfiguration.DisableAllGestures();
                    //    handConfiguration.ApplyChanges();
                    //}
                    

                    if (instance.AcquireFrame(true) < pxcmStatus.PXCM_STATUS_NO_ERROR)
                    {
                        break;
                    }

                    frameCounter++;

                    if (!DisplayDeviceConnection(!instance.IsConnected()))
                    {

                        if (handData != null)
                        {
                            handData.Update();
                        }

                        PXCMCapture.Sample sample = instance.QueryHandSample();
                        if (sample != null && sample.depth != null)
                        {
                            DisplayPicture(sample.depth, handData);

                            if (handData != null)
                            {
                                frameNumber = liveCamera ? frameCounter : instance.captureManager.QueryFrameIndex(); 

                                DisplayJoints(handData);

                                int value = DisplayGesture(handData);
                                if (value == -1)
                                    value = DisplayHandOrientation(handData);

                                valueFilter(value);
                                
                                DisplayAlerts(handData, frameNumber);
                            }
                            form.UpdatePanel();
                        }
                        timer.Tick();
                    }
                    instance.ReleaseFrame();
                }
            }
            else
            {
                form.UpdateStatus("Init Failed");
                flag = false;
            }
            foreach (PXCMImage pxcmImage in m_images)
            {
                pxcmImage.Dispose();
            }

            // Clean Up
            if (handData != null) handData.Dispose();
            if (handConfiguration != null) handConfiguration.Dispose();
            instance.Close();
            instance.Dispose();

            if (flag)
            {
                form.UpdateStatus("Stopped");
            }
        }

        private int valueFilter(int value)
        {
            for (int j = 1; j < valueShift.Length - 1; j++)
            {
                valueShift[j] = valueShift[j - 1];
            }
            valueShift[0] = value;

            for (int j = 1; j < valueShift.Length - 1; j++)
            {
                if (valueShift[j] != valueShift[0])
                    return -1;
            }

            if (valueShift[0] != -1)
            {
                form.UpdateInfo("Final: " + value + "\n", Color.SeaGreen);

                sendSocket(value);
                return value;
            }
            else
            {
                return -1;
            }
        }


        private Socket sender;
        public void StartSocket()
        {
            // Data buffer for incoming data.
            byte[] bytes = new byte[1024];

            // Connect to a remote device.
            try
            {
                // Establish the remote endpoint for the socket.
                // This example uses port 11000 on the local computer.
                //IPHostEntry ipHostInfo = Dns.Resolve(Dns.GetHostName());
                //IPAddress ipAddress = ipHostInfo.AddressList[0];

                IPAddress ipAddress = IPAddress.Parse(IPString);
                IPEndPoint remoteEP = new IPEndPoint(ipAddress, 11111);

                // Create a TCP/IP  socket.
                sender = new Socket(AddressFamily.InterNetwork,
                    SocketType.Stream, ProtocolType.Tcp);

                // Connect the socket to the remote endpoint. Catch any errors.
                sender.Connect(remoteEP);
                Console.WriteLine("Connected to socket");

                Console.WriteLine("Socket connected to {0}", sender.RemoteEndPoint.ToString());
                sender.NoDelay = true;
                sender.SendBufferSize = 16;
                // Encode the data string into a byte array.
                //byte[] msg = Encoding.ASCII.GetBytes("This is a test<EOF>");

                // Send the data through the socket.
                //int bytesSent = sender.Send(msg);

                //long test = 0;
                //while (true)
                //{
                //    test++;
                //10.202.113.62
                //msg = Encoding.ASCII.GetBytes(test.ToString() + "\n");
                //bytesSent = sender.Send(msg);
                    
                //}

                // Receive the response from the remote device.
                //int bytesRec = sender.Receive(bytes);
                //Console.WriteLine("Echoed test = {0}",
                //   Encoding.ASCII.GetString(bytes,0,bytesRec));

                // Release the socket.
                //sender.Shutdown(SocketShutdown.Both);
                //sender.Close();

            }
            catch (ArgumentNullException ane)
            {
                Console.WriteLine("ArgumentNullException : {0}", ane.ToString());
            }
            catch (SocketException se)
            {
                Console.WriteLine("SocketException : {0}", se.ToString());
            }
            catch (Exception e)
            {
                Console.WriteLine("Unexpected exception : {0}", e.ToString());
            }
        }

        public void sendSocket(int value)
        {
            // Encode the data string into a byte array.
            byte[] msg = Encoding.ASCII.GetBytes(value.ToString() + "\n");

            sender.NoDelay = true;
            // Send the data through the socket.
            int bytesSent = sender.Send(msg);

            

            //byte[] msg = new byte[1];
            //msg[0] = (byte)value;

            // Send the data through the socket.
            //int bytesSent = sender.Send(msg);
            //Console.WriteLine("Send: " + msg);

        }
    }
}
