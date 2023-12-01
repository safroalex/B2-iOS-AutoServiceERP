//
//  ServiceNetworkManager.swift
//  AutoServiceERP-Frontend
//
//  Created by Александр Сафронов on 01.12.2023.
//

import Foundation

class ServiceNetworkManager {
    static let shared = ServiceNetworkManager()

    private init() {}

    func addService(service: Service, completion: @escaping (Bool) -> Void) {
        guard let url = URL(string: "http://localhost:8080/api/services") else {
            print("Invalid URL")
            completion(false)
            return
        }

        var request = URLRequest(url: url)
        request.httpMethod = "POST"
        request.addValue("application/json", forHTTPHeaderField: "Content-Type")

        do {
            let jsonData = try JSONEncoder().encode(service)
            request.httpBody = jsonData
        } catch {
            print("Failed to encode service")
            completion(false)
            return
        }

        URLSession.shared.dataTask(with: request) { data, response, error in
            if let error = error {
                print("Error making request: \(error.localizedDescription)")
                completion(false)
                return
            }

            guard let httpResponse = response as? HTTPURLResponse, httpResponse.statusCode == 200 else {
                print("Invalid response from server")
                completion(false)
                return
            }

            completion(true)
        }.resume()
    }
    
    func fetchAllServices(completion: @escaping ([Service]) -> Void) {
        guard let url = URL(string: "http://localhost:8080/api/services") else {
            print("Invalid URL")
            completion([])
            return
        }

        var request = URLRequest(url: url)
        request.httpMethod = "GET"

        URLSession.shared.dataTask(with: request) { data, response, error in
            if let error = error {
                print("Error making request: \(error.localizedDescription)")
                completion([])
                return
            }

            guard let httpResponse = response as? HTTPURLResponse, httpResponse.statusCode == 200 else {
                print("Invalid response from server")
                completion([])
                return
            }

            guard let data = data else {
                print("No data received from server")
                completion([])
                return
            }

            do {
                let services = try JSONDecoder().decode([Service].self, from: data)
                completion(services)
            } catch {
                print("Error decoding JSON: \(error)")
                completion([])
            }
        }.resume()
    }

    func updateService(serviceId: Int, updatedService: Service, completion: @escaping (Bool) -> Void) {
        guard let url = URL(string: "http://localhost:8080/api/services/\(serviceId)") else {
            print("Invalid URL")
            completion(false)
            return
        }

        var request = URLRequest(url: url)
        request.httpMethod = "PUT"
        request.addValue("application/json", forHTTPHeaderField: "Content-Type")

        do {
            let jsonData = try JSONEncoder().encode(updatedService)
            request.httpBody = jsonData
        } catch {
            print("Failed to encode service")
            completion(false)
            return
        }

        URLSession.shared.dataTask(with: request) { data, response, error in
            if let error = error {
                print("Error making request: \(error.localizedDescription)")
                completion(false)
                return
            }

            guard let httpResponse = response as? HTTPURLResponse, httpResponse.statusCode == 200 else {
                print("Invalid response from server")
                completion(false)
                return
            }

            completion(true)
        }.resume()
    }

    func deleteService(serviceId: Int, completion: @escaping (Bool) -> Void) {
        guard let url = URL(string: "http://localhost:8080/api/services/\(serviceId)") else {
            print("Invalid URL")
            completion(false)
            return
        }

        var request = URLRequest(url: url)
        request.httpMethod = "DELETE"

        URLSession.shared.dataTask(with: request) { _, response, error in
            if let error = error {
                print("Error making request: \(error.localizedDescription)")
                completion(false)
                return
            }

            guard let httpResponse = response as? HTTPURLResponse, httpResponse.statusCode == 200 else {
                print("Invalid response from server")
                completion(false)
                return
            }

            completion(true)
        }.resume()
    }

}
