import tkinter as tk
import random
import math

# 창 크기
WINDOW_SIZE = 600
# 로버 크기
ROVER_SIZE = 20
# 장애물 크기
OBSTACLE_SIZE = int(ROVER_SIZE * 1.5)
# 감지 거리
SENSOR_DISTANCE = 50
# 격자 크기
GRID_SIZE = OBSTACLE_SIZE
# 초기 속도
INITIAL_SPEED = 1

# 장애물 패턴 정의
OBSTACLE_PATTERNS = [
    [(0, 0), (1, 0), (0, 1), (1, 1)],  # 'ㅁ' 모양
    [(0, 0), (0, 1), (0, 2), (0, 3)],  # 'I' 모양
    [(0, 0), (1, 0), (1, 1), (1, 2)],  # 'L' 모양
    [(0, 0), (1, 0), (2, 0), (1, 1)],  # 'T' 모양
    [(0, 0), (1, 0), (1, 1), (2, 1)],  # 'Z' 모양
]

class Rover:
    def __init__(self, canvas, x, y):
        self.canvas = canvas
        self.x = x
        self.y = y
        self.angle = 0
        self.speed = INITIAL_SPEED
        self.rover = self.canvas.create_rectangle(self.x, self.y, self.x + ROVER_SIZE, self.y + ROVER_SIZE, fill="blue")
        self.sensor_line = self.canvas.create_line(self.x, self.y, self.x, self.y, fill="red")

    def move(self):
        steps = int(self.speed)
        for _ in range(steps):
            dx = math.cos(math.radians(self.angle))
            dy = math.sin(math.radians(self.angle))
            self.canvas.move(self.rover, dx, dy)
            self.x += dx
            self.y += dy
            self.update_sensor()
            if self.detect_collision():
                break

    def update_sensor(self):
        sensor_x = self.x + ROVER_SIZE / 2 + SENSOR_DISTANCE * math.cos(math.radians(self.angle))
        sensor_y = self.y + ROVER_SIZE / 2 + SENSOR_DISTANCE * math.sin(math.radians(self.angle))
        self.canvas.coords(self.sensor_line, self.x + ROVER_SIZE / 2, self.y + ROVER_SIZE / 2, sensor_x, sensor_y)

    def detect_collision(self):
        sensor_x = self.x + ROVER_SIZE / 2 + SENSOR_DISTANCE * math.cos(math.radians(self.angle))
        sensor_y = self.y + ROVER_SIZE / 2 + SENSOR_DISTANCE * math.sin(math.radians(self.angle))
        items = self.canvas.find_overlapping(sensor_x, sensor_y, sensor_x, sensor_y)
        for item in items:
            if item != self.rover and item != self.sensor_line:
                self.change_direction()
                return True
        return False

    def change_direction(self):
        # 우회전
        self.angle = (self.angle + 90) % 360

    def increase_speed(self):
        self.speed += 1

    def decrease_speed(self):
        if self.speed > 1:
            self.speed -= 1

class Obstacle:
    def __init__(self, canvas, x, y):
        self.canvas = canvas
        self.x = x
        self.y = y
        self.blocks = []
        self.create_obstacle(x, y)

    def create_obstacle(self, x, y):
        pattern = random.choice(OBSTACLE_PATTERNS)
        for dx, dy in pattern:
            block_x = x + dx * GRID_SIZE
            block_y = y + dy * GRID_SIZE
            block = self.canvas.create_rectangle(block_x, block_y, block_x + OBSTACLE_SIZE, block_y + OBSTACLE_SIZE, fill="black")
            self.blocks.append(block)

class App:
    def __init__(self, root):
        self.root = root
        self.root.title("Rover Simulation")
        self.canvas = tk.Canvas(root, width=WINDOW_SIZE, height=WINDOW_SIZE)
        self.canvas.pack()
        self.rovers = []
        self.obstacles = []
        self.create_rover(WINDOW_SIZE / 2, WINDOW_SIZE / 2)
        self.create_walls()
        self.create_obstacles(5)
        self.root.bind("<Return>", self.add_rover)
        self.root.bind("<space>", self.add_obstacle)
        self.root.bind("<Up>", self.increase_speed)
        self.root.bind("<Down>", self.decrease_speed)
        self.run()

    def create_rover(self, x, y):
        rover = Rover(self.canvas, x, y)
        self.rovers.append(rover)

    def create_walls(self):
        self.canvas.create_line(0, 0, WINDOW_SIZE, 0, fill="black")
        self.canvas.create_line(0, 0, 0, WINDOW_SIZE, fill="black")
        self.canvas.create_line(WINDOW_SIZE, 0, WINDOW_SIZE, WINDOW_SIZE, fill="black")
        self.canvas.create_line(0, WINDOW_SIZE, WINDOW_SIZE, WINDOW_SIZE, fill="black")

    def create_obstacles(self, count):
        for _ in range(count):
            while True:
                x, y = self.get_random_grid_position()
                if not self.is_overlap(x, y):
                    break
            obstacle = Obstacle(self.canvas, x, y)
            self.obstacles.append(obstacle)

    def get_random_grid_position(self):
        grid_x = random.randint(0, (WINDOW_SIZE // GRID_SIZE) - 1) * GRID_SIZE
        grid_y = random.randint(0, (WINDOW_SIZE // GRID_SIZE) - 1) * GRID_SIZE
        return grid_x, grid_y

    def is_overlap(self, x, y):
        for dx, dy in random.choice(OBSTACLE_PATTERNS):
            block_x = x + dx * GRID_SIZE
            block_y = y + dy * GRID_SIZE
            overlap_items = self.canvas.find_overlapping(block_x, block_y, block_x + OBSTACLE_SIZE, block_y + OBSTACLE_SIZE)
            for item in overlap_items:
                if item in [rover.rover for rover in self.rovers] or item in [block for obstacle in self.obstacles for block in obstacle.blocks]:
                    return True
        return False

    def add_rover(self, event):
        x, y = self.get_random_grid_position()
        self.create_rover(x, y)

    def add_obstacle(self, event):
        while True:
            x, y = self.get_random_grid_position()
            if not self.is_overlap(x, y):
                break
        obstacle = Obstacle(self.canvas, x, y)
        self.obstacles.append(obstacle)

    def increase_speed(self, event):
        for rover in self.rovers:
            rover.increase_speed()

    def decrease_speed(self, event):
        for rover in self.rovers:
            rover.decrease_speed()

    def run(self):
        for rover in self.rovers:
            rover.move()
        self.root.after(50, self.run)

root = tk.Tk()
app = App(root)
root.mainloop()
