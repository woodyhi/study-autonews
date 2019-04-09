import os
import time

x = 1080
y = 1920

while True:
    print("---轮次---")

    # 广告按钮
    # os.system("adb shell input tap 1050 140")

    # 领取金币 转动
    os.system("adb shell input tap 540 1053")

    # 立即领取，立即翻倍，观看视频 领取翻倍卡
    os.system("adb shell input tap 540 1380")

    time.sleep(4)
