# This block configures Terraform itself.
terraform {
  required_version = ">= 1.0" # Specifies the minimum Terraform version required.
  required_providers {
    google = {
      source  = "hashicorp/google"
      version = "~> 5.0"
    }
  }
}

# This block configures the Google Cloud provider.
# Terraform will use the credentials configured in your gcloud CLI.
provider "google" {
  project = "sm-sandbox-467916" # Sostituisci con il tuo Project ID di Google Cloud
  region  = "europe-west1"      # Esempio: Belgio. Puoi scegliere una regione europea vicina.
}

/**
 * Creates a Virtual Private Cloud (VPC) network to house our services.
 * All our components (Kubernetes cluster, databases) will live inside this network.
 */
resource "google_compute_network" "vpc_network" {
  name                    = "risk-processor-vpc"
  auto_create_subnetworks = true # For simplicity, we let GCP create subnetworks automatically.
}

/**
 * Creates a Google Kubernetes Engine (GKE) cluster.
 * This cluster will run our containerized applications.
 */
resource "google_container_cluster" "primary_cluster" {
  name     = "risk-processor-cluster"
  location = "europe-west1" // Use the same region defined in the provider

  # We connect the cluster to the VPC we created earlier.
  network = google_compute_network.vpc_network.name

  # Define the pool of virtual machines (nodes) for the cluster.
  initial_node_count = 1 # A good starting point for a small project
  node_config {
    machine_type = "e2-standard-4" # A good, cost-effective machine type for this project
    oauth_scopes = [
      "https://www.googleapis.com/auth/cloud-platform"
    ]
  }

  # Disable deletion protection for the cluster.
  deletion_protection = false
}