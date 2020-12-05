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


# requests_count <= requests_sum <= requests_count * max_units_request
def generate_test(file_prefix, threads_count, requests_count, max_units_request, requests_sum):
    requests = generate_random_integers(requests_sum, requests_count, 1, max_units_request)
    chunks = np.array_split(requests, threads_count)
    for i in range(threads_count):
        with open(f"../../out/production/laby/{file_prefix}{i}", "w") as file:
            for number in chunks[i]:
                file.write(f"{number}\n")


def run_scenario(java, scenario):
    java_arguments_names = [
        "producerFilePrefix",
        "consumerFilePrefix",
        "producerCount",
        "consumerCount",
        "bufferSize",
        "maxUnitsRequest",
        "timeQuantum",
        "primaryTaskLength",
        "secondaryTaskLength"
    ]

    java_arguments_values = map(lambda argument_name: str(scenario[argument_name]), java_arguments_names)
    java_arguments_str = " ".join(java_arguments_values)

    generate_test(
        scenario["producerFilePrefix"],
        scenario["producerCount"],
        scenario["requestsCount"],
        scenario["maxUnitsRequest"],
        scenario["requestsSum"],
    )

    generate_test(
        scenario["consumerFilePrefix"],
        scenario["consumerCount"],
        scenario["requestsCount"],
        scenario["maxUnitsRequest"],
        scenario["requestsSum"],
    )

    synchronous_start = time.time()
    os.system(f"{java} synchronous {java_arguments_str}")
    synchronous_end = time.time()
    synchronous_time = synchronous_end - synchronous_start


    ao_start = time.time()
    os.system(f"{java} ao {java_arguments_str}")
    ao_end = time.time()
    ao_time = ao_end - ao_start

    return synchronous_time, ao_time

def run_from_config():
    config = json.load(open("config.json"))
    java = config["java"]
    scenarios = config["scenarios"]
    for scenario in scenarios:
        name = scenario["name"]
        base_parameters = scenario["baseParameters"]
        variants = scenario["variants"]
        print(name)
        for variant in variants:
            print(f"\t{variant}")
            synchronous_time, ao_time = run_scenario(java, {**base_parameters, **variant})
            print(f"\t\tSynchronous time: {synchronous_time}")
            print(f"\t\tAO time: {ao_time}")


run_from_config()