# Use the official Jenkins inbound agent with JDK 21
FROM jenkins/inbound-agent:latest-jdk21

# Switch to root to install packages
USER root

# Update and install Docker client (minimal)
RUN apt-get update --allow-releaseinfo-change && \
    apt-get install -y --no-install-recommends docker.io ca-certificates && \
    rm -rf /var/lib/apt/lists/*

# Switch back to the jenkins user (safer)
USER jenkins