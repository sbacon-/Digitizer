using System;
using System.Diagnostics;
using System.Windows.Input;

namespace Digitizer
{
    class Program
    {
        static void Main(string[] args)
        {
            Process p = new Process();

            DateTime now = DateTime.Now;
            System.Diagnostics.ProcessStartInfo processStartInfoClear =
                new System.Diagnostics.ProcessStartInfo("cmd", @"/c C:\Users\house\AppData\Local\Android\Sdk\platform-tools\adb logcat -c");
            /*
            System.Diagnostics.ProcessStartInfo processStartInfoNoAir =
                new System.Diagnostics.ProcessStartInfo("cmd", @"/c C:\Users\house\AppData\Local\Android\Sdk\platform-tools\adb pm disable com.samsung.android.service.aircommand");
            */
            System.Diagnostics.ProcessStartInfo processStartInfo =
                new System.Diagnostics.ProcessStartInfo("cmd", @"/c C:\Users\house\AppData\Local\Android\Sdk\platform-tools\adb logcat *:I");
            processStartInfo.UseShellExecute = false;
            processStartInfo.RedirectStandardOutput = true;

            p.StartInfo = processStartInfoClear;
            p.Start();
            p.WaitForExit(); 
            //p.StartInfo = processStartInfoNoAir;
            //p.Start();
            //p.WaitForExit();

            p.StartInfo = processStartInfo;

            p.Start();

            bool middleMouseState = false;
            float x, y, buttonMask, downMask;
            String trimPattern = "I DIGITIZER: ";
            while (true)
            {

                String lineIn = p.StandardOutput.ReadLine();
                if (lineIn == null || !lineIn.Contains(trimPattern)) continue;
                lineIn = lineIn.Substring(lineIn.IndexOf(trimPattern) + trimPattern.Length);
                String[] arr = lineIn.Split(' ');
                
                x = float.Parse(arr[0].Substring(2));
                y = float.Parse(arr[1].Substring(2));
                buttonMask = float.Parse(arr[2].Substring(2));
                downMask = float.Parse(arr[3].Substring(2));


                if (buttonMask == 0)
                {
                    if (middleMouseState)
                    {
                        MouseOperations.MouseEvent(MouseOperations.MouseEventFlags.MiddleUp);
                        middleMouseState = !middleMouseState;
                    }
                    if (downMask == 0) MouseOperations.MouseEvent(MouseOperations.MouseEventFlags.LeftDown);
                    if (downMask == 1) MouseOperations.MouseEvent(MouseOperations.MouseEventFlags.LeftUp);
                }
                if (buttonMask == 32 && !middleMouseState)
                {
                    middleMouseState = true;
                    MouseOperations.MouseEvent(MouseOperations.MouseEventFlags.MiddleDown);
                }
                MouseOperations.SetCursorPosition((int)x, (int)y);

                foreach (String s in arr) Console.WriteLine(s);
            }
            p.WaitForExit();
        }
        [System.Runtime.InteropServices.DllImport("user32.dll")]
        public static extern void mouse_event(int dwFlags, int dx, int dy, int cButtons, int dwExtraInfo);

        public const int MOUSEEVENTF_LEFTDOWN = 0x02;
        public const int MOUSEEVENTF_LEFTUP = 0x04;

    }
}
