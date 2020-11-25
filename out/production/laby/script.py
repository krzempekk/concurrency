import os
import threading
import time

best_len = 10000

def file_len(filename):
    with open(filename) as f:
        for i, l in enumerate(f):
            pass
    return i + 1

def run_java():
    os.system("/home/kamil/.jdks/openjdk-15/bin/java -javaagent:/opt/idea-IU-202.7660.26/lib/idea_rt.jar=43963:/opt/idea-IU-202.7660.26/bin -Dfile.encoding=UTF-8 -classpath /home/kamil/studia/5_semestr/tw/laby/out/production/laby lab4.zad1.ProducersConsumers > new_output")

for i in range(1, 1000):
    thread = threading.Thread(target=run_java)
    thread.start()
    time.sleep(1)
    os.system("kill $(ps aux | grep ProducersConsumers | grep -v 'grep' | awk '{print $2}')")
    new_len = file_len("new_output")
    print(f"new_len = {new_len}, best_len = {best_len}")
    if new_len < best_len:
        best_len = new_len
        os.system("mv new_output output")