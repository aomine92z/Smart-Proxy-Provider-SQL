# Proxy Provider Service usage:

## General information:

The Proxy Provider Service is a Java class that facilitates the selection and utilization of proxies for web scraping tasks. It includes functionality to find matching proxies based on website and proxy characteristics, call the proxy provider service, and retrieve the probability of a proxy's success.

The Proxy Provider Service has the following dependencies:

- Java (version 8 or higher)
- JPmml (Java library for working with PMML models)
- SQLite JDBC (Java Database Connectivity library for SQLite)

Make sure these dependencies are properly installed and configured in your Java development environment before using the Proxy Provider Service.

## How It Works:

The Proxy Provider Service works by providing a mechanism to select suitable proxies for web scraping tasks based on the characteristics of the website and the available proxies. It uses a combination of historical probabilities, simulation data, and random selection to determine the proxy's likelihood of success.

The main class in the Proxy Provider Service is `PP_Service`, which contains the following inner class:

* ServiceRunner: This class implements the Runnable interface and represents a thread that performs the proxy selection and calling of the proxy provider service. It takes several parameters including the list of URLs to be processed, the list of available proxies, maps to store the responses from the proxy provider service, start time, number of tries, and random selection instance.

* callProxyProvider: This method is responsible for calling the proxy provider service with the given arguments. It iterates over the list of URLs, selects a matching proxy for each URL based on type and country, retrieves the historical probability and timestamp for the selected proxy from the simulation data, and updates the response map accordingly. The method recursively calls itself with the remaining URLs until all URLs are processed or the maximum number of tries is reached.

* findMatchingProxy: This method is used to find a matching proxy for a given URL. It iterates over the list of available proxies and checks if the proxy type and country match the URL's type and country. It returns the first matching proxy found or null if no match is found.

* getTime: This method calculates the elapsed time since the start of the service and returns it in the format "minutes:seconds:centiseconds".

* getProba: This method retrieves the probability of a proxy's success based on the simulation data. It checks the current hour parity and compares it with the working hours specified in the simulation data. If there is a match, it returns the probability and the current timestamp.
