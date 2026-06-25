#!/bin/bash

# Đường dẫn file JAR và Class Client của ông
JAR_FILE="target/ttt-network-1.0-SNAPSHOT.jar"
CLIENT_CLASS="vgu.pe2026.ttt.net.Client"

echo "========================================="
echo "Bắt đầu thả 10000 con Client vào Server..."
echo "========================================="

for i in {1..10000}
do
    # Kích hoạt Client dưới background (dấu &).
    # Dùng lệnh (sleep 3; echo "q") để giả lập việc người chơi ngồi xem 3 giây rồi gõ 'q' thoát.
    # Chọn ID = 1 (hoặc 2) để Server nhận diện.
    (sleep 10;echo "q") | java -cp $JAR_FILE $CLIENT_CLASS 1 &
    
done

# echo "========================================="
# echo "Đã quăng xong 10 connections! Đang chờ tụi nó chơi và tự động thoát..."
# wait
# echo "Hoàn tất bài Test! Server vẫn sống khỏe chứ bro?"