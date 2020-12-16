import json
import os
import time
import random
import numpy as np
import matplotlib.pyplot as plt

synch_color = (49 / 255, 92 / 255, 43 / 255, 0.5)
ao_color = (233 / 255, 79 / 255, 55 / 255, 0.5)


def plot_2d(title, x_name, x_values, sync_values, ao_values, filename):
    plt.title(title)
    plt.xlabel(x_name)
    plt.ylabel("Time (s)")
    plt.scatter(x_values, sync_values, label="Synchronous", color=synch_color)
    plt.scatter(x_values, ao_values, label="AO", color=ao_color)
    plt.legend()
    plt.savefig(filename, dpi=600, bbox_inches='tight')
    # plt.show()
    plt.clf()


def plot_3d(title, x_name, x_values, y_name, y_values, sync_values, ao_values, filename):
    fig = plt.figure()
    ax = fig.add_subplot(111, projection='3d')
    ax.set_title(title)
    ax.set_xlabel(x_name)
    ax.set_ylabel(y_name)
    ax.set_zlabel('Time (s)')
    ax.plot_wireframe(x_values, y_values, sync_values, label="Synchronous", color=synch_color)
    ax.plot_wireframe(x_values, y_values, ao_values, label="AO", color=ao_color)
    # ax.legend()
    fig.savefig(filename, dpi=600, bbox_inches='tight')
    # plt.show()
    plt.clf()


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


def run_scenario(java, scenario, runs_number=1):
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

    synchronous_time = 0
    ao_time = 0

    synchronous_times = []
    ao_times = []
    for i in range(runs_number):
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
        # synchronous_time += synchronous_end - synchronous_start
        synchronous_times.append(synchronous_end - synchronous_start)

        ao_start = time.time()
        os.system(f"{java} ao {java_arguments_str}")
        ao_end = time.time()
        # ao_time += ao_end - ao_start
        ao_times.append(ao_end - ao_start)

    return np.median(synchronous_times), np.median(ao_times)


def run_from_config():
    config = json.load(open("config.json"))
    java = config["java"]
    scenarios = config["scenarios"]
    filtered_scenarios = filter(lambda scenario: not scenario.get("disabled"), scenarios)
    for scenario in filtered_scenarios:
        name = scenario["name"]
        base_parameters = scenario["baseParameters"]
        runs_number = scenario["runsNumber"]
        variants = scenario["variants"]
        axes = scenario["axes"]
        x_name, y_name = axes.get("x"), axes.get("y")
        x_values, y_values, synch_values, ao_values = [], [], [], []
        is_3d = y_name is not None

        print(name)
        for variant in variants:
            synchronous_time, ao_time = run_scenario(java, {**base_parameters, **variant}, runs_number)

            x_values.append(variant[x_name])
            if is_3d:
                y_values.append(variant[y_name])
            synch_values.append(synchronous_time)
            ao_values.append(ao_time)

            print(f"\t{variant}")
            print(f"\t\tSynchronous time: {synchronous_time}")
            print(f"\t\tAO time: {ao_time}")

        if is_3d:
            plot_3d("", x_name, x_values, y_name, y_values, synch_values, ao_values, f"figures/{name}.png")
        else:
            plot_2d("", x_name, x_values, synch_values, ao_values, f"figures/{name}.png")


run_from_config()

# fig = plt.figure(figsize=plt.figaspect(0.25))
#
# ax = fig.add_subplot(131, projection='3d')
# ax.plot_trisurf(side_tasks, sync_work, sync_side, color=(1, 0.2, 0.8, 0.7))
#
# ax = fig.add_subplot(132)
# ax.plot(side_tasks, async_side)
#
# ax = fig.add_subplot(133)
# ax.plot(side_tasks, sync_side, color='red')
