# How to run datastar-spring-mvc using Docker Compose

## Prometheus config

For the Prometheus container in the compose.yaml file to run, some permissions need to be set.

Run the following command from the folder with the **compose.yaml** file:
```bash
sudo chown -R 65534:65534 ./prometheus/
```

To allow Prometheus to scrape the metrics of the host server edit the prometheus.yml file.

```bash
sudo nano prometheus/prometheus.yml
```

Change **yourserverip** under the raspberry-pi scrape config to the IP if your host machine.

## Grafana config

For the Grafana container in the compose.yaml file to run, some permissions need to be set.

Run the following command from the folder with the **compose.yaml** file:
```bash
sudo chown -R 472:472 ./grafana/
```

Grafana also needs a default admin user.

Run the following 2 commands to set a username and password before running the Docker compose command.

```bash
export ADMIN_USER=<your username>
```

```bash
export ADMIN_PASSWORD=<your password>
```

## Raspberry PI hardware metrics

In order to export Raspberry PI hardware metrics to Prometheus, the following needs to be done.

### Create Node Exporter directory

```bash
sudo mkdir /opt/node-exporter
```

### Change directory to Node Exporter directory

```bash
cd /opt/node-exporter
```

### Download Node Exporter

The latest version URL is found at:
[Node Exported download URL](https://github.com/prometheus/node_exporter/releases/tag/v1.10.2)

```bash
sudo wget -O node-exporter.tar.gz https://github.com/prometheus/node_exporter/releases/download/v1.10.2/node_exporter-1.10.2.linux-armv7.tar.gz
```

### Extract Node Exporter

```bash
sudo tar -xvf node-exporter.tar.gz --strip-components=1
```

### Delete Node Exporter download file

```bash
sudo rm node-exporter.tar.gz
```

### Run Node Exporter to see if the installation worked

```bash
./node_exporter
```

Node Exporter should be available at **http://\<hostname\>:9100/**

### Run the following command to begin writing to a new file

```bash
sudo nano /etc/systemd/system/nodeexporter.service
```

### Within this file, copy in the following lines of code

```bash
[Unit]
Description=Prometheus Node Exporter
Documentation=https://prometheus.io/docs/guides/node-exporter/
After=network-online.target

[Service]
Restart=on-failure

ExecStart=/opt/node-exporter/node_exporter

[Install]
WantedBy=multi-user.target
```

### Enable the Node Exporter service

```bash
sudo systemctl enable nodeexporter
```

### Start the Node Exporter service

```bash
sudo systemctl start nodeexporter
```

### View the status of the Node Exporter service

```bash
sudo systemctl status nodeexporter
```
The output should be: **Active: active (running)**

## Run Docker Compose

Run the following Docker Compose command

```bash
docker compose up -d
```