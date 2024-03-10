//
//  TopMastersNetworkManager.swift
//  AutoServiceERP-Frontend
//
//  Created by Александр Сафронов on 10.03.2024.
//

import Foundation

class TopMastersNetworkManager {
    static let shared = TopMastersNetworkManager()

    func fetchTopMasters(fromDate: String, toDate: String, completion: @escaping (Result<[MasterStatistic], Error>) -> Void) {
        guard let url = URL(string: "http://localhost:8080/api/works/top-masters") else {
            print("Invalid URL")
            completion(.failure(NSError(domain: "InvalidURL", code: -1, userInfo: nil)))
            return
        }

        var components = URLComponents(url: url, resolvingAgainstBaseURL: false)
        components?.queryItems = [
            URLQueryItem(name: "fromDate", value: fromDate),
            URLQueryItem(name: "toDate", value: toDate)
        ]

        guard let finalURL = components?.url else {
            print("Invalid URL components")
            completion(.failure(NSError(domain: "InvalidURLComponents", code: -1, userInfo: nil)))
            return
        }

        var request = URLRequest(url: finalURL)
        request.httpMethod = "GET"

        print("Making request to URL: \(finalURL.absoluteString)")

        URLSession.shared.dataTask(with: request) { data, response, error in
            if let error = error {
                print("Error making request: \(error.localizedDescription)")
                completion(.failure(error))
                return
            }

            if let httpResponse = response as? HTTPURLResponse {
                print("HTTP Status Code: \(httpResponse.statusCode)")
                if let responseHeaders = httpResponse.allHeaderFields as? [String: Any] {
                    print("Response Headers: \(responseHeaders)")
                }
            }

            guard let data = data, let httpResponse = response as? HTTPURLResponse, httpResponse.statusCode == 200 else {
                print("Invalid response from server")
                completion(.failure(NSError(domain: "InvalidResponse", code: -1, userInfo: nil)))
                return
            }

            if let responseString = String(data: data, encoding: .utf8) {
                print("Response JSON String: \(responseString)")
            }

            do {
                let decoder = JSONDecoder()
                let mastersData = try decoder.decode([MasterStatistic].self, from: data)
                completion(.success(mastersData))
            } catch {
                print("Failed to decode top masters: \(error.localizedDescription)")
                completion(.failure(error))
            }
        }.resume()
    }
}

