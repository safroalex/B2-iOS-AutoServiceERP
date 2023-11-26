//
//  NetworkManager.swift
//  AutoServiceERP-Frontend
//
//  Created by Александр Сафронов on 24.11.2023.
//

import Foundation

class NetworkManager {
    static let shared = NetworkManager()

    private init() {}

    func fetchMasters(completion: @escaping ([Master]) -> Void) {
        guard let url = URL(string: "http://localhost:8080/api/masters") else {
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
                let masters = try JSONDecoder().decode([Master].self, from: data)
                completion(masters)
            } catch {
                print("Error decoding JSON: \(error)")
                completion([])
            }
        }.resume()
    }
    
    func addMaster(master: Master, completion: @escaping (Bool) -> Void) {
        guard let url = URL(string: "http://localhost:8080/api/masters") else {
            print("Invalid URL")
            completion(false)
            return
        }

        var request = URLRequest(url: url)
        request.httpMethod = "POST"
        request.addValue("application/json", forHTTPHeaderField: "Content-Type")

        do {
            let jsonData = try JSONEncoder().encode(master)
            request.httpBody = jsonData
        } catch {
            print("Failed to encode master")
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

    func updateMaster(masterId: Int, updatedMaster: Master, completion: @escaping (Bool) -> Void) {
        guard let url = URL(string: "http://localhost:8080/api/masters/\(masterId)") else {
            print("Invalid URL")
            completion(false)
            return
        }

        var request = URLRequest(url: url)
        request.httpMethod = "PUT"
        request.addValue("application/json", forHTTPHeaderField: "Content-Type")

        do {
            let jsonData = try JSONEncoder().encode(updatedMaster)
            request.httpBody = jsonData
        } catch {
            print("Failed to encode master")
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
    
    func deleteMaster(masterId: Int, completion: @escaping (Bool) -> Void) {
        guard let url = URL(string: "http://localhost:8080/api/masters/\(masterId)") else {
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

