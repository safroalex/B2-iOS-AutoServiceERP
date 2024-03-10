//
//  TotalCost.swift
//  AutoServiceERP-Frontend
//
//  Created by Александр Сафронов on 10.03.2024.
//

import Foundation

class TotalCostNetworkManager {
    static let shared = TotalCostNetworkManager()

    private init() {}

    func fetchTotalCost(fromDate: String, toDate: String, completion: @escaping (Result<Double, Error>) -> Void) {
        guard let url = URL(string: "http://localhost:8080/api/works/total-cost") else {
            print("Invalid URL")
            completion(.failure(NSError(domain: "NetworkError", code: -1, userInfo: [NSLocalizedDescriptionKey: "Invalid URL"])))
            return
        }

        var components = URLComponents(url: url, resolvingAgainstBaseURL: false)
        components?.queryItems = [
            URLQueryItem(name: "fromDate", value: fromDate),
            URLQueryItem(name: "toDate", value: toDate)
        ]

        guard let finalURL = components?.url else {
            print("Invalid URL components")
            completion(.failure(NSError(domain: "NetworkError", code: -1, userInfo: [NSLocalizedDescriptionKey: "Invalid URL components"])))
            return
        }

        var request = URLRequest(url: finalURL)
        request.httpMethod = "GET"
        request.addValue("application/json", forHTTPHeaderField: "Content-Type")

        print("Making request to URL: \(finalURL.absoluteString)")

        URLSession.shared.dataTask(with: request) { data, response, error in
            if let error = error {
                print("Error making request: \(error.localizedDescription)")
                completion(.failure(error))
                return
            }

            guard let httpResponse = response as? HTTPURLResponse else {
                print("Invalid response from server")
                completion(.failure(NSError(domain: "NetworkError", code: -1, userInfo: [NSLocalizedDescriptionKey: "Invalid response from server"])))
                return
            }

            print("HTTP Status Code: \(httpResponse.statusCode)")
            print("Response Headers: \(httpResponse.allHeaderFields)")

            if httpResponse.statusCode != 200 {
                print("Error: Server responded with status code \(httpResponse.statusCode)")
                completion(.failure(NSError(domain: "NetworkError", code: httpResponse.statusCode, userInfo: [NSLocalizedDescriptionKey: "Server responded with error"])))
                return
            }

            guard let data = data else {
                print("No data received from server")
                completion(.failure(NSError(domain: "NetworkError", code: -1, userInfo: [NSLocalizedDescriptionKey: "No data received from server"])))
                return
            }

            if let responseString = String(data: data, encoding: .utf8) {
                print("Response JSON String: \(responseString)")
            }

            do {
                let decoder = JSONDecoder()
                let totalCostData = try decoder.decode(TotalCost.self, from: data)
                completion(.success(totalCostData.totalCost))
            } catch {
                print("Failed to decode total cost: \(error.localizedDescription)")
                completion(.failure(error))
            }
        }.resume()
    }
}
