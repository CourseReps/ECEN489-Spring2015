using System;
using System.Collections;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;

namespace hands_viewer.cs
{
    public partial class MainForm : Form
    {
        public PXCMSession g_session;
        private volatile bool closing = false;
        public volatile bool stop = false;
        private Bitmap bitmap = null;


        private Hashtable pictures;
        private Timer timer = new Timer();
        private string filename = null;
        public Dictionary<string, PXCMCapture.DeviceInfo> Devices { get; set; }

        private string gestureName = null;
        private bool _isInitGesturesFirstTime = false;

        private const string smootherWeightedDefaultValue = "10.0";
        private const string smootherStabilizerDefaultValue = "40.0";
        private const string smootherSpringDefaultValue = "5.0";
        private const string smootherQuadraticDefaultValue = "0.1";

        private class Item
        {
            public string Name;
            public int Value;
            public Item(string name, int value)
            {
                Name = name; Value = value;
            }
            public override string ToString()
            {
                // Generates the text shown in the combo box
                return Name;
            }
        }


        public MainForm(PXCMSession session)
        {
            InitializeComponent();

            this.g_session = session;
            PopulateDeviceMenu();
            PopulateModuleMenu();
            
            //cmbGesturesList.Enabled = false;
            FormClosing += new FormClosingEventHandler(MainForm_FormClosing);
            Panel2.Paint += new PaintEventHandler(Panel_Paint);
            timer.Tick += new EventHandler(timer_Tick);
            timer.Interval = 2000;
            timer.Start();
        }



        //private delegate void UpdateGesturesToListDelegate(string gestureName, int index);
        //public void UpdateGesturesToList(string gestureName, int index)
        //{
        //    cmbGesturesList.Invoke(new UpdateGesturesToListDelegate(delegate(string name, int cmbIndex) { cmbGesturesList.Items.Add(new Item(name, cmbIndex)); }), new object[] { gestureName, index });
        //}

        //public void setInitGesturesFirstTime(bool isInit)
        //{
        //    _isInitGesturesFirstTime = isInit;

        //}


        //private delegate void UpdateGesturesListSizeDelegate();
        //public void UpdateGesturesListSize()
        //{
        //    cmbGesturesList.Invoke(new UpdateGesturesListSizeDelegate(delegate() { cmbGesturesList.Enabled = true; cmbGesturesList.Size = new System.Drawing.Size(100, 70); }), new object[] { });

        //}

        public bool getInitGesturesFirstTime()
        {
            return _isInitGesturesFirstTime;
        }



        public string GetCheckedSmoother()
        {
            foreach (ToolStripMenuItem m in MainMenu.Items)
            {
                if (!m.Text.Equals("Smoother")) continue;
                foreach (ToolStripMenuItem e in m.DropDownItems)
                {
                    if (e.Checked) return e.Text;
                }
            }
            return null;
        }

        private void PopulateDeviceMenu()
        {
            Devices = new Dictionary<string, PXCMCapture.DeviceInfo>();

            PXCMSession.ImplDesc desc = new PXCMSession.ImplDesc();
            desc.group = PXCMSession.ImplGroup.IMPL_GROUP_SENSOR;
            desc.subgroup = PXCMSession.ImplSubgroup.IMPL_SUBGROUP_VIDEO_CAPTURE;
            ToolStripMenuItem sm = new ToolStripMenuItem("Device");
            for (int i = 0; ; i++)
            {
                PXCMSession.ImplDesc desc1;
                if (g_session.QueryImpl(desc, i, out desc1) < pxcmStatus.PXCM_STATUS_NO_ERROR) break;
                PXCMCapture capture;
                if (g_session.CreateImpl<PXCMCapture>(desc1, out capture) < pxcmStatus.PXCM_STATUS_NO_ERROR) continue;
                for (int j = 0; ; j++)
                {
                    PXCMCapture.DeviceInfo dinfo;
                    if (capture.QueryDeviceInfo(j, out dinfo) < pxcmStatus.PXCM_STATUS_NO_ERROR) break;
                    string name = dinfo.name;
                    if (Devices.ContainsKey(dinfo.name))
                    {
                        name += j;
                    }
                    Devices.Add(name, dinfo);
                    ToolStripMenuItem sm1 = new ToolStripMenuItem(dinfo.name, null, new EventHandler(Device_Item_Click));
                    sm.DropDownItems.Add(sm1);
                }
                capture.Dispose();
            }
            if (sm.DropDownItems.Count > 0)
                (sm.DropDownItems[0] as ToolStripMenuItem).Checked = true;
            MainMenu.Items.RemoveAt(0);
            MainMenu.Items.Insert(0, sm);
        }

        private void PopulateModuleMenu()
        {
            PXCMSession.ImplDesc desc = new PXCMSession.ImplDesc();
            desc.cuids[0] = PXCMHandModule.CUID;
            ToolStripMenuItem mm = new ToolStripMenuItem("Module");
            for (int i = 0; ; i++)
            {
                PXCMSession.ImplDesc desc1;
                if (g_session.QueryImpl(desc, i, out desc1) < pxcmStatus.PXCM_STATUS_NO_ERROR) break;
                ToolStripMenuItem mm1 = new ToolStripMenuItem(desc1.friendlyName, null, new EventHandler(Module_Item_Click));
                mm.DropDownItems.Add(mm1);
            }
            if (mm.DropDownItems.Count > 0)
                (mm.DropDownItems[0] as ToolStripMenuItem).Checked = true;
            MainMenu.Items.RemoveAt(1);
            MainMenu.Items.Insert(1, mm);
        }


        private void RadioCheck(object sender, string name)
        {
            foreach (ToolStripMenuItem m in MainMenu.Items)
            {
                if (!m.Text.Equals(name)) continue;
                foreach (ToolStripMenuItem e1 in m.DropDownItems)
                {
                    e1.Checked = (sender == e1);
                }
            }
        }

        private void Device_Item_Click(object sender, EventArgs e)
        {
            RadioCheck(sender, "Device");
        }

        private void Module_Item_Click(object sender, EventArgs e)
        {
            RadioCheck(sender, "Module");
        }





        private void Start_Click(object sender, EventArgs e)
        {
            MainMenu.Enabled = false;
            //Start.Enabled = false;
            //Stop.Enabled = true;

            stop = false;
            System.Threading.Thread thread = new System.Threading.Thread(DoRecognition);
            thread.Start();
            System.Threading.Thread.Sleep(5);
        }

        delegate void DoRecognitionCompleted();
        public void DoRecognition()
        {
            HandsRecognition gr = new HandsRecognition(this);
            gr.SimplePipeline();
            this.Invoke(new DoRecognitionCompleted(
                delegate
                {
                    //Start.Enabled = true;
                    //Stop.Enabled = false;
                    MainMenu.Enabled = true;
                    if (closing) Close();
                }
            ));
        }

        public string GetCheckedDevice()
        {
            foreach (ToolStripMenuItem m in MainMenu.Items)
            {
                if (!m.Text.Equals("Device")) continue;
                foreach (ToolStripMenuItem e in m.DropDownItems)
                {
                    if (e.Checked) return e.Text;
                }
            }
            return null;
        }

        public string GetCheckedModule()
        {
            foreach (ToolStripMenuItem m in MainMenu.Items)
            {
                if (!m.Text.Equals("Module")) continue;
                foreach (ToolStripMenuItem e in m.DropDownItems)
                {
                    if (e.Checked) return e.Text;
                }
            }
            return null;
        }

        //public bool GetDepthState()
        //{
        //    return Depth.Checked;
        //}

        //public bool GetLabelmapState()
        //{
        //    return Labelmap.Checked;
        //}

        //public bool GetJointsState()
        //{
        //    return Joints.Checked;
        //}

        //public bool GetSkeletonState()
        //{
        //    return Skeleton.Checked;
        //}

        private void MainForm_FormClosing(object sender, FormClosingEventArgs e)
        {
            stop = true;
            //e.Cancel = Stop.Enabled;
            closing = true;
        }

        private delegate void UpdateStatusDelegate(string status);
        public void UpdateStatus(string status)
        {
            Status2.Invoke(new UpdateStatusDelegate(delegate(string s) { StatusLabel.Text = s; }), new object[] { status });
        }

        private delegate void UpdateInfoDelegate(string status, Color color);
        public void UpdateInfo(string status, Color color)
        {
            infoTextBox.Invoke(new UpdateInfoDelegate(delegate(string s, Color c)
            {
                if (status == String.Empty)
                {
                    infoTextBox.Text = String.Empty;
                    return;
                }

                if (infoTextBox.TextLength > 50000)
                {
                    infoTextBox.Text = String.Empty;
                }

                infoTextBox.SelectionColor = c;

                infoTextBox.SelectedText = s;
                infoTextBox.SelectionColor = infoTextBox.ForeColor;

                infoTextBox.SelectionStart = infoTextBox.Text.Length;
                infoTextBox.ScrollToCaret();

            }), new object[] { status, color });
        }

        //private delegate void UpdateFPSStatusDelegate(string status);
        public void updateXlabel(Label label, string text)
        {
            label.Invoke(new UpdateFPSStatusDelegate(delegate(string s) { label.Text = s; }), new object[] { text });
        }


        private delegate void UpdateFPSStatusDelegate(string status);
        public void UpdateFPSStatus(string status)
        {
            labelFPS.Invoke(new UpdateFPSStatusDelegate(delegate(string s) { labelFPS.Text = s; }), new object[] { status });
        }

        private void Stop_Click(object sender, EventArgs e)
        {
            stop = true;
        }

        public void DisplayBitmap(Bitmap picture)
        {
            lock (this)
            {
                if (bitmap != null)
                    bitmap.Dispose();
                bitmap = new Bitmap(picture);
            }
        }

        private void Panel_Paint(object sender, PaintEventArgs e)
        {
            lock (this)
            {
                if (bitmap == null || bitmap.Width == 0 || bitmap.Height == 0) return;
                Bitmap bitmapNew = new Bitmap(bitmap);
                try
                {
                    //if (Mirror.Checked)
                    //{
                    bitmapNew.RotateFlip(RotateFlipType.RotateNoneFlipX);
                    //}

                    //if (Scale2.Checked)
                    //{
                    /* Keep the aspect ratio */
                    Rectangle rc = (sender as PictureBox).ClientRectangle;
                    float xscale = (float)rc.Width / (float)bitmap.Width;
                    float yscale = (float)rc.Height / (float)bitmap.Height;
                    float xyscale = (xscale < yscale) ? xscale : yscale;
                    int width = (int)(bitmap.Width * xyscale);
                    int height = (int)(bitmap.Height * xyscale);
                    rc.X = (rc.Width - width) / 2;
                    rc.Y = (rc.Height - height) / 2;
                    rc.Width = width;
                    rc.Height = height;
                    e.Graphics.DrawImage(bitmapNew, rc);
                    //}
                    //else
                    //{
                    //    e.Graphics.DrawImageUnscaled(bitmapNew, 0, 0);
                    //}
                }
                finally
                {
                    bitmapNew.Dispose();
                }
            }
        }

        public void DisplayJoints(PXCMHandData.JointData[][] nodes, int numOfHands)
        {
            if (bitmap == null) return;
            if (nodes == null) return;

            //if (Joints.Checked || Skeleton.Checked)
            //{
            lock (this)
            {
                int scaleFactor = 1;

                Graphics g = Graphics.FromImage(bitmap);

                using (Pen boneColor = new Pen(Color.DodgerBlue, 3.0f))
                {
                    for (int i = 0; i < numOfHands; i++)
                    {
                        if (nodes[i][0] == null) continue;
                        int baseX = (int)nodes[i][0].positionImage.x / scaleFactor;
                        int baseY = (int)nodes[i][0].positionImage.y / scaleFactor;

                        int wristX = (int)nodes[i][0].positionImage.x / scaleFactor;
                        int wristY = (int)nodes[i][0].positionImage.y / scaleFactor;

                        //if (Skeleton.Checked)
                        //{
                        for (int j = 1; j < 22; j++)
                        {
                            if (nodes[i][j] == null) continue;
                            int x = (int)nodes[i][j].positionImage.x / scaleFactor;
                            int y = (int)nodes[i][j].positionImage.y / scaleFactor;

                            if (nodes[i][j].confidence <= 0) continue;

                            if (j == 2 || j == 6 || j == 10 || j == 14 || j == 18)
                            {

                                baseX = wristX;
                                baseY = wristY;
                            }

                            g.DrawLine(boneColor, new Point(baseX, baseY), new Point(x, y));
                            baseX = x;
                            baseY = y;
                        }
                        //}

                        //if (Joints.Checked)
                        //{
                        using (
                            Pen red = new Pen(Color.Red, 3.0f),
                                black = new Pen(Color.Black, 3.0f),
                                green = new Pen(Color.Green, 3.0f),
                                blue = new Pen(Color.Blue, 3.0f),
                                cyan = new Pen(Color.Cyan, 3.0f),
                                yellow = new Pen(Color.Yellow, 3.0f),
                                orange = new Pen(Color.Orange, 3.0f))
                        {
                            Pen currnetPen = black;

                            for (int j = 0; j < PXCMHandData.NUMBER_OF_JOINTS; j++)
                            {
                                float sz = 4;
                                //if (Labelmap.Checked)
                                //    sz = 2;

                                int x = (int)nodes[i][j].positionImage.x / scaleFactor;
                                int y = (int)nodes[i][j].positionImage.y / scaleFactor;

                                if (nodes[i][j].confidence <= 0) continue;

                                //Wrist
                                if (j == 0)
                                {
                                    currnetPen = black;
                                }

                                //Center
                                if (j == 1)
                                {
                                    currnetPen = red;
                                    sz += 4;
                                }

                                //Thumb
                                if (j == 2 || j == 3 || j == 4 || j == 5)
                                {
                                    currnetPen = green;
                                }
                                //Index Finger
                                if (j == 6 || j == 7 || j == 8 || j == 9)
                                {
                                    currnetPen = blue;
                                }
                                //Finger
                                if (j == 10 || j == 11 || j == 12 || j == 13)
                                {
                                    currnetPen = yellow;
                                }
                                //Ring Finger
                                if (j == 14 || j == 15 || j == 16 || j == 17)
                                {
                                    currnetPen = cyan;
                                }
                                //Pinkey
                                if (j == 18 || j == 19 || j == 20 || j == 21)
                                {
                                    currnetPen = orange;
                                }


                                if (j == 5 || j == 9 || j == 13 || j == 17 || j == 21)
                                {
                                    sz += 4;
                                    //currnetPen.Width = 1;
                                }

                                g.DrawEllipse(currnetPen, x - sz / 2, y - sz / 2, sz, sz);
                            }
                        }
                        //}



                    }

                }
                g.Dispose();
            }
            //}
        }

        //private delegate void DisplayGesturesDelegate(PXCMHandData.GestureData gestureData, uint handID);
        //public void DisplayGestures(PXCMHandData.GestureData gestureData, uint handID)
        //{
        //    if (!Gesture.Checked) return;

        //    if (handID == 1)
        //    {
        //        Gesture1.Invoke(new DisplayGesturesDelegate(delegate(PXCMHandData.GestureData data, uint id)
        //        {
        //                Gesture1.Image = (Bitmap) pictures[data.name];
        //                Gesture1.Invalidate();
        //                timer.Start();

        //        }), new object[] {gestureData, handID});
        //    }

        //    if (handID == 2)
        //    {
        //        Gesture2.Invoke(new DisplayGesturesDelegate(delegate(PXCMHandData.GestureData data, uint id)
        //        {         
        //                Gesture2.Image = (Bitmap) pictures[data.name];
        //                Gesture2.Invalidate();
        //                timer.Start();

        //        }), new object[] {gestureData, handID});
        //    }
        //}

        private delegate void UpdatePanelDelegate();
        public void UpdatePanel()
        {

            Panel2.Invoke(new UpdatePanelDelegate(delegate()
            {
                Panel2.Invalidate();
            }));

        }

        private void simpleToolStripMenuItem_Click(object sender, EventArgs e)
        {
            RadioCheck(sender, "Pipeline");
        }

        private void advancedToolStripMenuItem_Click(object sender, EventArgs e)
        {
            RadioCheck(sender, "Pipeline");
        }

        private void timer_Tick(object sender, EventArgs e)
        {
            //UpdateLeftGesturesStatus("");
            //UpdateRightGesturesStatus("");
        }

        private void Live_Click(object sender, EventArgs e)
        {
            Playback.Checked = Record.Checked = false;
            Live.Checked = true;
        }

        private void Playback_Click(object sender, EventArgs e)
        {
            Live.Checked = Record.Checked = false;
            Playback.Checked = true;

            OpenFileDialog ofd = new OpenFileDialog();
            ofd.Filter = "RSSDK clip|*.rssdk|Old format clip|*.pcsdk|All files|*.*";
            ofd.CheckFileExists = true;
            ofd.CheckPathExists = true;
            filename = (ofd.ShowDialog() == DialogResult.OK) ? ofd.FileName : null;
        }

        public bool GetPlaybackState()
        {
            return Playback.Checked;
        }



        private void Record_Click(object sender, EventArgs e)
        {
            Live.Checked = Playback.Checked = false;
            Record.Checked = true;
            SaveFileDialog sfd = new SaveFileDialog();
            sfd.Filter = "RSSDK clip|*.rssdk|All Files|*.*";
            sfd.CheckPathExists = true;
            sfd.OverwritePrompt = true;
            sfd.AddExtension = true;
            filename = (sfd.ShowDialog() == DialogResult.OK) ? sfd.FileName : null;
        }

        public bool GetRecordState()
        {
            return Record.Checked;
        }

        public string GetFileName()
        {
            return filename;
        }

        //private void cmbGesturesList_SelectedIndexChanged(object sender, EventArgs e)
        //{
        //    gestureName = cmbGesturesList.SelectedItem.ToString();
        //}

        public string GetGestureName()
        {
            return gestureName;
        }

    }
}
