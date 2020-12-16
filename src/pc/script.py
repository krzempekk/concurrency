import json
import os
import time
import random
import numpy as np
import matplotlib.pyplot as plt

# green
synch_color = (49 / 255, 92 / 255, 43 / 255, 0.8)
# orange
ao_color = (233 / 255, 79 / 255, 55 / 255, 0.8)


def plot_2d(x_name, x_values, sync_values, ao_values, filename):
    plt.xlabel(x_name)
    plt.ylabel("Time (s)")
    plt.scatter(x_values, sync_values, label="Synchronous", color=synch_color)
    plt.scatter(x_values, ao_values, label="AO", color=ao_color)
    plt.legend()
    plt.savefig(f"{filename}.png", dpi=600, bbox_inches='tight')
    # plt.show()
    plt.clf()


def plot_3d(x_name, x_values, y_name, y_values, sync_values, ao_values, filename):
    fig = plt.figure()
    ax = fig.add_subplot(111, projection='3d')
    ax.set_xlabel(x_name)
    ax.set_ylabel(y_name)
    ax.set_zlabel('Time (s)')

    x_values, y_values = np.meshgrid(x_values, y_values)
    sync_values = np.array(sync_values)
    ao_values = np.array(ao_values)

    ax.plot_surface(x_values, y_values, sync_values, color=synch_color, edgecolor=synch_color, alpha=0.3)
    ax.plot_surface(x_values, y_values, ao_values, color=ao_color, edgecolor=ao_color, alpha=0.3)

    fig.savefig(f"{filename}.png", dpi=600, bbox_inches='tight')
    # plt.show()
    plt.clf()

    ax.plot_surface(x_values, y_values, sync_values, color=synch_color, edgecolor=synch_color, alpha=0.3)

    fig = plt.figure()
    ax = fig.add_subplot(111, projection='3d')
    ax.set_xlabel(x_name)
    ax.set_ylabel(y_name)
    ax.set_zlabel('(SYNC - AO) / AO')
    values = (np.array(sync_values) - np.array(ao_values)) / np.array(ao_values)
    ax.plot_surface(x_values, y_values, values, color=synch_color, edgecolor=synch_color, alpha=0.3)
    fig.savefig(f"{filename}-diff-rel.png", dpi=600, bbox_inches='tight')
    plt.clf()

    ax.plot_surface(x_values, y_values, sync_values, color=synch_color, edgecolor=synch_color, alpha=0.3)

    fig = plt.figure()
    ax = fig.add_subplot(111, projection='3d')
    ax.set_xlabel(x_name)
    ax.set_ylabel(y_name)
    ax.set_zlabel('SYNC - AO')
    values = (np.array(sync_values) - np.array(ao_values))
    ax.plot_surface(x_values, y_values, values, color=synch_color, edgecolor=synch_color, alpha=0.3)
    fig.savefig(f"{filename}-diff.png", dpi=600, bbox_inches='tight')
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


class Scenario:
    def __init__(self, name, base_parameters, runs_number, test_values, axes, test_runner):
        self.name = name
        self.base_parameters = base_parameters
        self.runs_number = runs_number
        self.test_values = test_values
        self.test_runner = test_runner
        self.x_name, self.y_name = axes.get("x"), axes.get("y")
        self.is_3d = self.y_name is not None
        self.x_values = []
        self.y_values = []
        self.ao_values = []
        self.synch_values = []

    def run(self):
        print(self.name)

        if not self.is_3d:
            variant_keys = list(self.test_values.keys())
            for index in range(len(self.test_values[variant_keys[0]])):
                variant = {}
                for key in variant_keys:
                    variant[key] = self.test_values[key][index]
                self.process_variant(variant)
        else:
            for x_value in self.test_values[self.x_name]:
                for y_value in self.test_values[self.y_name]:
                    variant = {self.x_name: x_value, self.y_name: y_value}
                    self.process_variant(variant)

        if self.is_3d:
            x_test_values = self.test_values[self.x_name]
            y_test_values = self.test_values[self.y_name]
            test_values_count = len(x_test_values)
            synch_matrix = np.zeros((test_values_count, test_values_count))
            ao_matrix = np.zeros((test_values_count, test_values_count))
            for i in range(len(self.x_values)):
                x = self.x_values[i]
                y = self.y_values[i]
                synch_v = self.synch_values[i]
                ao_v = self.ao_values[i]
                synch_matrix[y_test_values.index(y)][x_test_values.index(x)] = synch_v
                ao_matrix[y_test_values.index(y)][x_test_values.index(x)] = ao_v

            plot_3d(self.x_name, x_test_values, self.y_name, y_test_values, synch_matrix, ao_matrix,
                    f"figures/{self.name}")
        else:
            plot_2d(self.x_name, self.x_values, self.synch_values, self.ao_values, f"figures/{self.name}")

    def process_variant(self, variant):
        synchronous_time, ao_time = self.test_runner.run_variant({**self.base_parameters, **variant}, self.runs_number)

        self.x_values.append(variant[self.x_name])
        if self.is_3d:
            self.y_values.append(variant[self.y_name])
        self.synch_values.append(synchronous_time)
        self.ao_values.append(ao_time)

        print(f"\t{variant}")
        print(f"\t\tSynchronous time: {synchronous_time}")
        print(f"\t\tAO time: {ao_time}")


class TestRunner:

    def __init__(self, config_file):
        config = json.load(open(config_file))
        self.java = config["java"]
        self.scenarios = []
        scenarios_config = filter(lambda sc: not sc.get("disabled"), config["scenarios"])
        for scenario in scenarios_config:
            name = scenario["name"]
            base_parameters = scenario["baseParameters"]
            runs_number = scenario["runsNumber"]
            test_values = scenario["testValues"]
            axes = scenario["axes"]
            self.scenarios.append(Scenario(name, base_parameters, runs_number, test_values, axes, self))

    def run_scenarios(self):
        for scenario in self.scenarios:
            scenario.run()

    def run_variant(self, variant, runs_number=1):
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

        java_arguments_values = map(lambda argument_name: str(variant[argument_name]), java_arguments_names)
        java_arguments_str = " ".join(java_arguments_values)

        synchronous_times = []
        ao_times = []
        for i in range(runs_number):
            generate_test(
                variant["producerFilePrefix"],
                variant["producerCount"],
                variant["requestsCount"],
                variant["maxUnitsRequest"],
                variant["requestsSum"],
            )

            generate_test(
                variant["consumerFilePrefix"],
                variant["consumerCount"],
                variant["requestsCount"],
                variant["maxUnitsRequest"],
                variant["requestsSum"],
            )

            synchronous_start = time.time()
            os.system(f"{self.java} synchronous {java_arguments_str}")
            synchronous_end = time.time()
            synchronous_times.append(synchronous_end - synchronous_start)

            ao_start = time.time()
            os.system(f"{self.java} ao {java_arguments_str}")
            ao_end = time.time()
            ao_times.append(ao_end - ao_start)

        return np.median(synchronous_times), np.median(ao_times)


runner = TestRunner("config-new-2.json")
runner.run_scenarios()

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
