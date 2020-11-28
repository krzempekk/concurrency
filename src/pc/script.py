import json
import os
import time
import random
import numpy as np


def generate_random_integers(sum, n, min_v, max_v):
    array = [min_v] * n
    diff = sum - min_v * n
    while diff > 0:
        a = random.randint(0, n - 1)
        if array[a] >= max_v:
            continue
        array[a] += 1
        diff -= 1
    return array


def generate_tests(producer_count, consumer_count, max_units_request):
    producer_request_amount = producer_count * 10000
    consumer_request_amount = consumer_count * 10000
    requests_sum = producer_request_amount * (max_units_request / 2)

    producer_requests = generate_random_integers(requests_sum, producer_request_amount, 1, max_units_request)
    consumer_requests = generate_random_integers(requests_sum, consumer_request_amount, 1, max_units_request)

    producer_chunks = np.array_split(producer_requests, producer_count)

    consumer_chunks = np.array_split(consumer_requests, consumer_count)

    for i in range(producer_count):
        with open(f"../../resources/producer{i}", "w") as file:
            for number in producer_chunks[i]:
                file.write(f"{number}\n")

    for i in range(consumer_count):
        with open(f"../../resources/consumer{i}", "w") as file:
            for number in consumer_chunks[i]:
                file.write(f"{number}\n")


def run():
    config = json.load(open("config.json"))
    java_ao = config['java']['ao']
    java_synchronous = config['java']['synchronous']

    arguments = config['arguments']
    arguments_str = f"{arguments['producer_count']} " \
                    f"{arguments['consumer_count']} " \
                    f"{arguments['buffer_size']} " \
                    f"{arguments['max_units_request']} " \
                    f"{arguments['producer_file_prefix']} " \
                    f"{arguments['consumer_file_prefix']}"

    generate_tests(int(arguments['producer_count']), int(arguments['consumer_count']),
                   int(arguments['max_units_request']))

    start = time.time()
    os.system(f"{java_ao} {arguments_str}")
    end = time.time()
    print(f"AO time: {end - start}")

    start = time.time()
    os.system(f"{java_synchronous} {arguments_str}")
    end = time.time()
    print(f"Synchronous time: {end - start}")


# generate_tests(6, 6, 10)
run()
