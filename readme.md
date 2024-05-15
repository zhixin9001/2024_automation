py -m venv auto

auto/Scripts/activate.ps1

pip3 install -r requirements.txt

start 'C:\Program Files (x86)\Microsoft\Edge\Application\msedge.exe' --remote-debugging-port=9222 --user-data-dir="C:\Users\Zhi.Xin\Personal\seleniumEdge"

C:\Program Files (x86)\Microsoft\Edge\Application\msedge.exe

taskkill /f /im msedge.exe