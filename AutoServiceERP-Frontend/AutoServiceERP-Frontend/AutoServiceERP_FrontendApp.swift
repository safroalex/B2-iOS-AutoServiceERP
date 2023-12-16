//
//  AutoServiceERP_FrontendApp.swift
//  AutoServiceERP-Frontend
//
//  Created by Александр Сафронов on 25.10.2023.
//

import SwiftUI

struct MainTabView: View {
    var body: some View {
        TabView {
            NavigationView {
                SpravochnikiHomeView()
            }
            .tabItem {
                Label("Справочники", systemImage: "list.dash")
            }

            NaznachenieRabotView()
                .tabItem {
                    Label("Назначения", systemImage: "calendar")
                }

            StatistikaView()
                .tabItem {
                    Label("Статистика", systemImage: "chart.bar")
                }
        }
    }
}



struct StatistikaView: View {
    var body: some View {
        Text("Аналитика")
    }
}

@main
struct AutoServiceERP_FrontendApp: App {
    @State private var isAuthenticated = false

    var body: some Scene {
        WindowGroup {
            if isAuthenticated {
                MainTabView()
            } else {
                LoginView(isAuthenticated: $isAuthenticated)
            }
        }
    }
}


